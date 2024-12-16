package com.epam.wca.gym.controller;

import com.epam.wca.gym.controller.impl.TrainingControllerImpl;
import com.epam.wca.gym.dto.training_type.TrainingTypeBasicDTO;
import com.epam.wca.gym.interceptor.LoggingInterceptor;
import com.epam.wca.gym.interceptor.UserDetailsInterceptor;
import com.epam.wca.gym.repository.UserRepository;
import com.epam.wca.gym.service.TrainingTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingControllerImpl.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
class TrainingControllerImplTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrainingTypeService trainingTypeService;

    @MockitoBean
    private LoggingInterceptor loggingInterceptor;

    @MockitoBean
    private UserDetailsInterceptor userDetailsInterceptor;

    @MockitoBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws IOException {
        when(loggingInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(userDetailsInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    void testGetTypes() throws Exception {
        List<TrainingTypeBasicDTO> mockTrainingTypes = new ArrayList<>();

        when(trainingTypeService.findAll())
                .thenReturn(mockTrainingTypes);

        mockMvc.perform(get("/trainings/types"))
                .andExpect(status().isOk());
    }
}
