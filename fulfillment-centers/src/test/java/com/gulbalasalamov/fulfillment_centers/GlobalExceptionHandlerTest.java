package com.gulbalasalamov.fulfillment_centers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.gulbalasalamov.fulfillment_centers.controller.ProductController;
import com.gulbalasalamov.fulfillment_centers.exception.GlobalExceptionHandler;
import com.gulbalasalamov.fulfillment_centers.exception.ProductNotFoundException;
import com.gulbalasalamov.fulfillment_centers.model.dto.ProductDTO;
import com.gulbalasalamov.fulfillment_centers.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(GlobalExceptionHandler.class)
@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new ProductController(productService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

//    @Test
//    public void testHandleInvalidFormatException() throws Exception { // Mock the behavior to throw InvalidFormatException
//        doThrow(new InvalidFormatException(null, "Invalid value", "invalid", String.class)).when(productService).saveProduct(any(ProductDTO.class));
//        mockMvc.perform(post("/api/v1/products/create").contentType(MediaType.APPLICATION_JSON).content("{\"id\":\"invalid\",\"productId\":\"P123\",\"status\":\"SELLABLE\",\"fulfillmentCenter\":\"FC1\",\"quantity\":10,\"value\":100.0}")).andExpect(status().isBadRequest()).andExpect(jsonPath("$.message").value("Invalid value"));
//    }

    @Test
    public void testHandleProductNotFoundException() throws Exception {
        when(productService.getProductById(999L)).thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/api/v1/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Product not found"));
    }

//    @Test
//    public void testHandleIllegalArgumentException() throws Exception {
//        doThrow(new IllegalArgumentException("Invalid status value")).when(productService).saveProduct(any(ProductDTO.class));
//
//        mockMvc.perform(post("/api/v1/products/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"productId\":\"P123\",\"status\":\"INVALID_STATUS\",\"fulfillmentCenter\":\"FC1\",\"quantity\":10,\"value\":100.0}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Invalid status value"));
//    }

    @Test
    public void testHandleAllExceptions() throws Exception {
        doThrow(new RuntimeException("Unexpected error")).when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/v1/products/delete/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }
}

