# 🚀 Padronização de Erros no Spring Boot

Este projeto demonstra como **padronizar respostas de erro** em APIs
Java usando **Spring Boot 3+**, seguindo a especificação [RFC 9457 -
Problem Details for HTTP
APIs](https://datatracker.ietf.org/doc/html/rfc9457).\
O objetivo é criar **payloads consistentes**, melhorar a **experiência
do desenvolvedor** e facilitar a **manutenção**.

------------------------------------------------------------------------

## 📌 Objetivos

-   Padronizar respostas de erro HTTP
-   Separar claramente **Client Errors (4xx)** e **Server Errors (5xx)**
-   Utilizar **ProblemDetail** para simplificar o retorno
-   Centralizar o tratamento de exceções com **@RestControllerAdvice**
-   Melhorar a **consistência** e a **DX** (Developer Experience)

------------------------------------------------------------------------

## 📜 RFC 9457 --- Problem Details

A RFC define um padrão para respostas de erro HTTP. Exemplo de payload
recomendado:

``` json
{
    "action": "Verifique se os parâmetros enviados na consulta estão certos.",
    "detail": "Produto com ID 15e183f5-27bd-4848-9bfa-0890401f70ee não encontrado.",
    "instance": "/products/15e183f5-27bd-4848-9bfa-890401f70ee",
    "status": 404,
    "timestamp": "2025-09-08T12:34:15.210220163-03:00",
    "title": "NotFoundException",
    "type": "https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status/404"
}
```

------------------------------------------------------------------------

## ⚡ Exceções Customizadas

O projeto separa as exceções em duas categorias:

### **Client Errors (4xx)**

Exceções lançadas quando o cliente envia dados inválidos ou solicitações
incorretas.

``` java
public final class NotFoundException extends BaseClientException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND,
              "Verifique os parâmetros enviados.",
              "NotFoundException");
    }
}
```

### **Server Errors (5xx)**

Exceções lançadas quando ocorrem falhas internas ou imprevistas no
servidor.

``` java
public final class InternalServerErrorException extends BaseServerException {
    public InternalServerErrorException() {
        super("Erro inesperado no servidor.",
              HttpStatus.INTERNAL_SERVER_ERROR,
              "Tente novamente mais tarde.",
              InternalServerErrorException.class.getSimpleName());
    }
}
```

------------------------------------------------------------------------

## 🛡️ Tratamento Global de Erros

Usamos um **@RestControllerAdvice** para centralizar todo o tratamento:

``` java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<ProblemDetail> handleBaseException(BaseHttpException ex, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        pd.setTitle(ex.getName());
        pd.setProperty("action", ex.getAction());
        pd.setProperty("timestamp", OffsetDateTime.now(ZoneOffset.UTC));
        pd.setInstance(URI.create(req.getRequestURI()));
        return ResponseEntity.status(ex.getStatus()).body(pd);
    }
}
```

------------------------------------------------------------------------

## 🧩 Exemplo de Uso --- ProductController

``` java
@GetMapping("/{id}")
public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Produto com ID " + id + " não encontrado."));
    return ResponseEntity.ok(new ProductResponse(product));
}
```

**Exemplo de resposta quando o produto não existe:**

``` json
{
  "type": "https://api.exemplo.com/errors/not-found",
  "title": "NotFoundException",
  "status": 404,
  "detail": "Produto com ID 123 não encontrado.",
  "action": "Verifique se os parâmetros enviados na consulta estão certos.",
  "timestamp": "2025-09-08T12:34:56Z",
  "instance": "/products/123"
}
```

------------------------------------------------------------------------

## 📂 Estrutura do Projeto

    src/main/java/br/dev/joaobarbosa/error_handling
    │
    ├── config
    │   └── GlobalExceptionHandler.java
    │   └── exceptions
    │       ├── BaseHttpException.java
    │       ├── client
    │       │   ├── BaseClientException.java
    │       │   └── NotFoundException.java
    │       ├── server
    │       │   ├── BaseServerException.java
    │       │   └── InternalServerErrorException.java
    │
    ├── modules
    │   └── products
    │       ├── ProductController.java
    │       ├── ProductRepository.java
    │       ├── dto
    │       │   ├── ProductRequest.java
    │       │   └── ProductResponse.java
    │
    └── ErrorHandlingApplication.java

------------------------------------------------------------------------

## 🛠️ Como Rodar o Projeto

``` bash
# Clone o repositório
git clone https://github.com/juaoantonio/error-handling.git

# Entre na pasta
cd error-handling

# Rode a aplicação
./gradlew bootRun
```

------------------------------------------------------------------------

## 🔗 Links Úteis

-   **RFC 9457** → <https://datatracker.ietf.org/doc/html/rfc9457>
-   **Spring ProblemDetail** → <https://spring.io/blog/problem-detail>
-   **Meu LinkedIn** → <https://linkedin.com/in/juaoantonio>

------------------------------------------------------------------------

## 📌 Autor

Feito com ❤️ por **João Antônio Barbosa**\
Se curtir, ⭐ o repositório e contribua! 🚀
