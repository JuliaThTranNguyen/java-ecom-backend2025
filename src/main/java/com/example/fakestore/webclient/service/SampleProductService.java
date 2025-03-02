package com.example.fakestore.webclient.service;

import com.example.fakestore.repository.ProductRepository;
import com.example.fakestore.webclient.dto.SampleProduct;
import com.example.fakestore.webclient.mapper.SampleProductMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class SampleProductService {
    private WebClient webClient;
    private ProductRepository productRepository;
    private SampleProductMapper mapper;

    public SampleProductService(ProductRepository productRepository, SampleProductMapper mapper) {
        this.webClient = WebClient.builder()
                .baseUrl("https://fakestoreapi.com")
                .build();
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

//    @PostConstruct
    public void fetchProduct() {
        log.info("start fetching...............");
        webClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(SampleProduct.class)
                .collectList() // Chuyển thành List<SampleProduct>;
                .subscribe(products -> products.forEach(product ->
                        productRepository.save(mapper.toProduct(product))));


    }
}
