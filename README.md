# Pharmacy Commerce API

E-commerce platform backend for pharmacy management - Built with Spring Boot and Java 17.

## 📋 Sobre o Projeto

Sistema de e-commerce completo para farmácias, implementado como monólito modular com potencial de expansão para múltiplas filiais. Inclui gestão de catálogo, preços, estoque, carrinho, checkout e pedidos.

## 🏗️ Arquitetura

### Monólito Modular

O projeto segue uma arquitetura de monólito modular com separação clara por domínios:

- **shared**: Exceções, DTOs base, enums e utilitários
- **auth**: Autenticação JWT e RBAC
- **store**: Gestão de lojas
- **catalog**: Produtos e descrições
- **category**: Hierarquia de categorias
- **brand**: Marcas
- **pricing**: Preços e promoções
- **inventory**: Controle de estoque
- **customer**: Cadastro de clientes
- **cart**: Carrinho de compras
- **checkout**: Finalização de compra
- **order**: Pedidos e status
- **cms**: Banners e conteúdo
- **audit**: Rastreabilidade

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 4.0.3**
- **Spring Security** + JWT
- **Spring Data JPA** + Hibernate
- **PostgreSQL** (banco principal)
- **Redis** (cache e sessão)
- **Flyway** (migrations)
- **Lombok**
- **Springdoc OpenAPI** (documentação)
- **JUnit 5** + Mockito (testes)

## 📦 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+
- Docker e Docker Compose (para PostgreSQL e Redis)
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code)

## 🔧 Configuração e Execução

### 1. Clonar o repositório

```bash
git clone <repository-url>
cd demo
```

### 2. Iniciar os serviços de infraestrutura

```bash
docker-compose up -d
```

Isso iniciará:
- PostgreSQL na porta 5432
- Redis na porta 6379

### 3. Compilar o projeto

```bash
./mvnw clean compile
```

### 4. Executar os testes

```bash
./mvnw test
```

### 5. Iniciar a aplicação

```bash
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## 📚 Documentação da API

Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 🔐 Autenticação

### Usuário Administrador Padrão

**Email**: `admin@farmacia.com`
**Senha**: `admin123`

### Fazer Login

```bash
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "admin@farmacia.com",
  "password": "admin123"
}
```

Resposta:

```json
{
  "success": true,
  "message": "Login realizado com sucesso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "email": "admin@farmacia.com",
    "name": "Administrador",
    "role": "ADMIN"
  },
  "timestamp": "2026-03-16T10:00:00Z"
}
```

### Usar o Token

Adicione o header em requisições autenticadas:

```
Authorization: Bearer {seu-token-aqui}
```

## 🗄️ Banco de Dados

### Migrations

O projeto utiliza Flyway para gerenciar o schema do banco. As migrations estão em:

```
src/main/resources/db/migration/
```

Total de 21 migrations implementadas:
- V001 a V005: Tabelas base (role, store, admin_user, customer, address)
- V006 a V011: Catálogo (category, brand, product, product_image, product_price, store_inventory)
- V012 a V013: Carrinho (cart, cart_item)
- V014 a V016: Pedidos (customer_order, order_item, order_status_history)
- V017: CMS (banner)
- V018: Auditoria (audit_log)
- V019 a V021: Seeds iniciais (roles, store, admin user)

### Conectar ao Banco

```bash
docker exec -it pharmacy-postgres psql -U pharmacy_user -d pharmacy_db
```

## 🏪 Dados Iniciais

### Roles
- ADMIN (acesso total)
- MANAGER (gerente de loja)

### Loja Padrão
- Nome: Farmácia Central
- Slug: farmacia-central
- Cidade: São Paulo, SP

## 🧪 Testes

Execute todos os testes:

```bash
./mvnw test
```

Execute um teste específico:

```bash
./mvnw test -Dtest=AuthServiceTest
```

## 📂 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/pharmacy/commerce/
│   │   ├── CommerceApiApplication.java
│   │   ├── shared/
│   │   ├── auth/
│   │   ├── customer/
│   │   ├── store/
│   │   ├── catalog/
│   │   ├── category/
│   │   ├── brand/
│   │   ├── pricing/
│   │   ├── inventory/
│   │   ├── cart/
│   │   ├── checkout/
│   │   ├── order/
│   │   ├── cms/
│   │   ├── notification/
│   │   └── audit/
│   └── resources/
│       ├── application.yml
│       ├── application-dev.yml
│       ├── application-test.yml
│       ├── application-prod.yml
│       └── db/migration/
└── test/
```

## 🔒 Segurança

- Senhas criptografadas com BCrypt
- Autenticação via JWT
- RBAC (Role-Based Access Control)
- CORS configurado
- Endpoints protegidos por role
- Auditoria de ações administrativas

## 🌐 Endpoints Principais

### Públicos
- `POST /api/v1/auth/login` - Login
- `POST /api/v1/customers/register` - Cadastro de cliente
- `GET /api/v1/products` - Listar produtos
- `GET /api/v1/categories` - Listar categorias
- `GET /api/v1/brands` - Listar marcas
- `GET /api/v1/search` - Busca de produtos

### Administrativos (requerem autenticação)
- `POST /api/v1/admin/products` - Criar produto
- `PUT /api/v1/admin/products/{id}` - Atualizar produto
- `POST /api/v1/admin/categories` - Criar categoria
- `POST /api/v1/admin/prices` - Definir preço
- `POST /api/v1/admin/inventory` - Atualizar estoque
- `GET /api/v1/admin/orders` - Listar pedidos
- `PATCH /api/v1/admin/orders/{id}/status` - Atualizar status

## 🔍 Monitoramento

Endpoints do Actuator:
- `GET /actuator/health` - Status da aplicação
- `GET /actuator/info` - Informações da aplicação
- `GET /actuator/metrics` - Métricas

## 📝 Variáveis de Ambiente

### Desenvolvimento (application-dev.yml)
Já configurado com valores padrão para ambiente local.

### Produção (application-prod.yml)
Requer as seguintes variáveis:

```bash
DATABASE_URL=jdbc:postgresql://host:5432/db
DATABASE_USERNAME=user
DATABASE_PASSWORD=pass
REDIS_HOST=redis-host
REDIS_PORT=6379
REDIS_PASSWORD=redis-pass
JWT_SECRET=your-secret-key-min-256-bits
```

## 📄 Licença

Este projeto foi desenvolvido para fins educacionais e comerciais.