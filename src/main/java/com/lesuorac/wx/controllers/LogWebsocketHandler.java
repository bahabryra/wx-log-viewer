package com.lesuorac.wx.controllers;

import static com.lesuorac.wx.lib.RuntimeWrapperException.wrap;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lesuorac.wx.lib.ClosableLock;
import com.lesuorac.wx.lib.RuntimeWrapperException;

@Component
public class LogWebsocketHandler extends TextWebSocketHandler {

    private static final Logger LOGGER = LogManager.getFormatterLogger();

    private Queue<WebSocketSession> sessions;
    private ClosableLock lock;

    public LogWebsocketHandler() {
        this.sessions = new ConcurrentLinkedQueue<>();
        this.lock = new ClosableLock();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        this.lock.performAction(() -> this.sessions.add(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        this.lock.performAction(() -> this.sessions.remove(session));
    }

    public void broadcast(Iterable<?> logs) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<TextMessage> messages = new ArrayList<>();
            logs.forEach(log -> messages.add(new TextMessage(wrap(() -> mapper.writeValueAsString(log)))));

            try (Closeable ‿ = this.lock.open()) {
                for (WebSocketSession session : this.sessions) {
                    messages.forEach(message -> wrap(() -> session.sendMessage(message)));
                }
            } catch (InterruptedException e) {
                LOGGER.error("Broadcasting messaged skiped.", e);
            }

        } catch (IOException | IllegalArgumentException e) {
            LOGGER.error("Cannot send message.", e);
        } catch (RuntimeWrapperException e) {
            LOGGER.error(e.getLocalizedMessage(), e.getUnderlyingException());
        }
    }

}
