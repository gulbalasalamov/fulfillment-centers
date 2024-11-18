package com.gulbalasalamov.fulfillment_centers;

import com.gulbalasalamov.fulfillment_centers.exception.ErrorDetails;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorDetailsTest {

    @Test
    public void testGetAndSetTimeStamp() {
        Date now = new Date();
        ErrorDetails errorDetails = new ErrorDetails(now, "Test message", "Test details");

        assertEquals(now, errorDetails.getTimeStamp());

        Date newDate = new Date(now.getTime() + 1000);
        errorDetails.setTimeStamp(newDate);
        assertEquals(newDate, errorDetails.getTimeStamp());
    }

    @Test
    public void testGetAndSetMessage() {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Test message", "Test details");

        assertEquals("Test message", errorDetails.getMessage());

        errorDetails.setMessage("New test message");
        assertEquals("New test message", errorDetails.getMessage());
    }

    @Test
    public void testGetAndSetDetails() {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), "Test message", "Test details");

        assertEquals("Test details", errorDetails.getDetails());

        errorDetails.setDetails("New test details");
        assertEquals("New test details", errorDetails.getDetails());
    }
}
