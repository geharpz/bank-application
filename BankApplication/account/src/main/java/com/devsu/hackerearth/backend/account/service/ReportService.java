package com.devsu.hackerearth.backend.account.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.account.dto.AccountDataDto;
import com.devsu.hackerearth.backend.account.dto.ClientData;
import com.devsu.hackerearth.backend.account.dto.ReportPeriod;
import com.devsu.hackerearth.backend.account.dto.ReportRequestEvent;
import com.devsu.hackerearth.backend.account.dto.ReportResponseEvent;
import com.devsu.hackerearth.backend.account.kafka.consumer.ReportRequestConsumer;
import com.devsu.hackerearth.backend.account.kafka.consumer.ReportResponseListener;
import com.devsu.hackerearth.backend.account.kafka.producer.ReportRequestProducer;
import com.devsu.hackerearth.backend.account.kafka.producer.ReportResponseProducer;
import com.devsu.hackerearth.backend.account.mapper.AccountDataMapper;
import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.repository.AccountRepository;
import com.devsu.hackerearth.backend.account.repository.TransactionRepository;
import com.devsu.hackerearth.backend.account.util.DateUtil;

import lombok.RequiredArgsConstructor;

/**
 * Service class responsible for handling the end-to-end workflow of
 * transaction-based reporting.
 * <p>
 * Encapsulates both asynchronous report generation (based on Kafka events) and
 * synchronous
 * retrieval logic for reports requested by clients. This class strictly follows
 * the Single Responsibility Principle
 * by separating the logic of report processing and HTTP response generation.
 * </p>
 *
 * Functional Responsibilities:
 * <ul>
 * <li>Collect account and transaction data based on a client ID and date
 * range.</li>
 * <li>Transform raw domain models into structured {@link ReportResponseEvent}
 * objects.</li>
 * <li>Dispatch report requests to Kafka and respond to correlation ID
 * lookups.</li>
 * <li>Support client-side polling of reports using correlation
 * identifiers.</li>
 * </ul>
 *
 * Dependencies:
 * <ul>
 * <li>{@link AccountRepository} – Fetches active client accounts.</li>
 * <li>{@link TransactionRepository} – Retrieves transactions per account within
 * a date range.</li>
 * <li>{@link AccountDataMapper} – Aggregates account and transaction data.</li>
 * <li>{@link ReportResponseProducer} – Publishes enriched report events to
 * Kafka.</li>
 * <li>{@link ReportResponseListener} – Retrieves processed reports by
 * correlation ID.</li>
 * <li>{@link ReportRequestConsumer} – Forwards report request events to
 * processing channels.</li>
 * </ul>
 * 
 * Author: Germán Ponce
 * Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class ReportService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ReportResponseProducer reportResponseProducer;
    private final AccountDataMapper accountDataMapper;
    private final ReportResponseListener reportResponseListener;
    private final ReportRequestProducer reportProducer;

    /**
     * Processes a report request event by aggregating data and sending the result
     * asynchronously.
     *
     * @param request the event containing client ID, date range, and correlation ID
     */
    public void processReportRequest(ReportRequestEvent request) {
        List<Account> accountList = accountRepository.findActiveByIdClient(request.getClientId()).orElse(null);
        List<AccountDataDto> accountDataList = new ArrayList<>(1);

        for (Account account : accountList) {
            List<Transaction> transactionList = transactionRepository.findByClientIdAndTransactionDateRange(
                    account.getId(), request.getStartDate(), request.getEndDate());

            accountDataList.add(accountDataMapper.toData(account, transactionList));
        }

        ReportPeriod period = new ReportPeriod();
        period.setFrom(DateUtil.formatDate(request.getStartDate(), DateUtil.YYYY_MM_DD));
        period.setTo(DateUtil.formatDate(request.getEndDate(), DateUtil.YYYY_MM_DD));

        ClientData client = new ClientData();
        client.setId(request.getClientId());

        ReportResponseEvent response = new ReportResponseEvent(
                client,
                period,
                accountDataList,
                request.getCorrelationId());

        reportResponseProducer.sendReportResponse(response);
    }

    /**
     * Handles a client-side request to either retrieve an existing report or
     * initiate a new one.
     *
     * @param clientId      the client whose transactions are being reported
     * @param start         start date of the report period
     * @param end           end date of the report period
     * @param correlationId optional identifier used to retrieve an existing report
     * @return appropriate HTTP response based on the operation
     */
    public ResponseEntity<Object> handleReportRequest(Long clientId, LocalDate start, LocalDate end,
            String correlationId) {
        if (correlationId != null) {
            return getExistingReport(correlationId);
        }
        return dispatchNewReport(clientId, start, end);
    }

    /**
     * Attempts to retrieve a report response using a correlation ID from the event
     * listener.
     *
     * @param correlationId unique identifier for the report
     * @return HTTP 200 if found; HTTP 404 with diagnostic message if not
     */
    private ResponseEntity<Object> getExistingReport(String correlationId) {
        ReportResponseEvent report = reportResponseListener.getReportByCorrelationId(correlationId);

        if (report != null) {
            return ResponseEntity.ok(report);
        }

        Map<String, Object> notReady = Map.of(
                "message", "Report not ready or correlation ID invalid",
                "correlationId", correlationId,
                "status", "NOT_FOUND");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notReady);
    }

    /**
     * Dispatches a new report request to the messaging system and returns tracking
     * information.
     *
     * @param clientId the ID of the client
     * @param start    start date for the report range
     * @param end      end date for the report range
     * @return HTTP 202 Accepted with metadata for client-side tracking
     */
    private ResponseEntity<Object> dispatchNewReport(Long clientId, LocalDate start, LocalDate end) {
        String correlationId = UUID.randomUUID().toString();

        ReportRequestEvent event = new ReportRequestEvent(clientId, start, end, correlationId);
        reportProducer.send(event);

        Map<String, Object> response = Map.of(
                "message", "Report requested. It will be available soon.",
                "correlationId", correlationId,
                "status", "PENDING",
                "reportUrl", String.format(
                        "/api/transactions/clients/%d/report?dateTransactionStart=%s&dateTransactionEnd=%s&correlationId=%s",
                        clientId, start, end, correlationId));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}