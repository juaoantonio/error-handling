package br.dev.joaobarbosa.error_handling.modules.products;

import br.dev.joaobarbosa.error_handling.config.exceptions.client.NotFoundException;
import br.dev.joaobarbosa.error_handling.modules.products.dto.ProductRequest;
import br.dev.joaobarbosa.error_handling.modules.products.dto.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> response = products.stream()
                .map(ProductResponse::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto com ID " + id + " não encontrado."));
        return ResponseEntity.ok(new ProductResponse(product));
    }

    @PostMapping
    public ResponseEntity<UUID> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Product product = new Product(productRequest);
        var result = this.productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(result.getId());
    }
}
