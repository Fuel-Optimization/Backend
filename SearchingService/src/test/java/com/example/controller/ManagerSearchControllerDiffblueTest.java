package com.example.controller;

import static org.mockito.Mockito.when;

import com.example.model.service.DriverService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ManagerSearchController.class})
@ExtendWith(SpringExtension.class)
class ManagerSearchControllerDiffblueTest {
    @MockBean
    private DriverService driverService;

    @Autowired
    private ManagerSearchController managerSearchController;

    /**
     * Method under test: {@link ManagerSearchController#searchByName(String)}
     */
    @Test
    void testSearchByName() throws Exception {
        // Arrange
        when(driverService.searchByName(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/search/by-name").param("name", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(managerSearchController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ManagerSearchController#searchByNationalId(String)}
     */
    @Test
    void testSearchByNationalId() throws Exception {
        // Arrange
        when(driverService.searchByNationalId(Mockito.<String>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/search/by-national-id")
                .param("nationalId", "foo");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(managerSearchController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
