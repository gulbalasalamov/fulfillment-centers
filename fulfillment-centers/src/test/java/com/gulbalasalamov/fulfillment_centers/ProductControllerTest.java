package com.gulbalasalamov.fulfillment_centers;


import com.gulbalasalamov.fulfillment_centers.controller.ProductController;
import com.gulbalasalamov.fulfillment_centers.model.dto.ProductDTO;
import com.gulbalasalamov.fulfillment_centers.model.enums.Status;
import com.gulbalasalamov.fulfillment_centers.response.TotalValueResponse;
import com.gulbalasalamov.fulfillment_centers.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductDTO productDTO;

    @BeforeEach
    public void setUp() {
        productDTO = new ProductDTO(1L, "P123", Status.SELLABLE, "FC1", 10, 100.0);
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(1L)).thenReturn(productDTO);

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value("P123"));

        verify(productService, times(1)).getProductById(1L);
    }

    @Test
    public void testGetAllProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(Collections.singletonList(productDTO));

        mockMvc.perform(get("/api/v1/products/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].productId").value("P123"));

        verify(productService, times(1)).getAllProducts();
    }

    @Test
    public void testGetProductByStatus() throws Exception {
        when(productService.getProductsByStatus("SELLABLE")).thenReturn(Collections.singletonList(productDTO));

        mockMvc.perform(get("/api/v1/products/status/SELLABLE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].productId").value("P123"));

        verify(productService, times(1)).getProductsByStatus("SELLABLE");
    }

    @Test
    public void testGetTotalValueByStatus() throws Exception {
        Status statusEnum = Status.valueOf("SELLABLE");
        TotalValueResponse totalValueResponse = new TotalValueResponse(100.0, statusEnum);
        when(productService.getTotalValuesByStatus("SELLABLE")).thenReturn(totalValueResponse);

        mockMvc.perform(get("/api/v1/products/total-value/status/SELLABLE"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"value\":100.0,\"status\":\"SELLABLE\"}"));

        verify(productService, times(1)).getTotalValuesByStatus("SELLABLE");
    }

    @Test
    public void testCreateProduct() throws Exception {
        when(productService.saveProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/v1/products/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"productId\":\"P123\",\"status\":\"SELLABLE\",\"fulfillmentCenter\":\"FC1\",\"quantity\":10,\"value\":100.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value("P123"));

        verify(productService, times(1)).saveProduct(any(ProductDTO.class));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(patch("/api/v1/products/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"productId\":\"P123\",\"status\":\"SELLABLE\",\"fulfillmentCenter\":\"FC1\",\"quantity\":10,\"value\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productId").value("P123"));

        verify(productService, times(1)).updateProduct(eq(1L), any(ProductDTO.class));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn("Product with id: 1 deleted successfully");

        mockMvc.perform(delete("/api/v1/products/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product with id: 1 deleted successfully"));

        verify(productService, times(1)).deleteProduct(1L);
    }
}

