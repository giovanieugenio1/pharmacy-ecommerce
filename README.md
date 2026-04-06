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

## Imagens do sistema

<img width="1273" height="781" alt="Captura de tela 2026-04-06 092941" src="https://github.com/user-attachments/assets/c7983d42-3d6e-4852-bc22-59513ffd7211" />
_______________________________________________________________________________________________________________________________________________________
<img width="1511" height="901" alt="Captura de tela 2026-04-06 092802" src="https://github.com/user-attachments/assets/70d3b42a-2cb4-4f63-a526-8ee06b62ddcd" />
_______________________________________________________________________________________________________________________________________________________

<img width="1362" height="913" alt="Captura de tela 2026-04-06 092911" src="https://github.com/user-attachments/assets/7b9bd59e-e644-4ae6-b33b-24ffd2da5c08" />
_______________________________________________________________________________________________________________________________________________________

<img width="1311" height="899" alt="Captura de tela 2026-04-06 092835" src="https://github.com/user-attachments/assets/296a5ad0-f82b-4842-b7bc-9361c0275e06" />
_______________________________________________________________________________________________________________________________________________________

<img width="1311" height="520" alt="Captura de tela 2026-04-06 092846" src="https://github.com/user-attachments/assets/a3e8cb21-953b-4be2-b060-a4196eb7192b" />
_______________________________________________________________________________________________________________________________________________________

## Imagens do sistema Web para ADMIN

<img width="1896" height="569" alt="Captura de tela 2026-04-06 104315" src="https://github.com/user-attachments/assets/ac146632-26c5-4350-b4cf-16c1977f021e" />
_______________________________________________________________________________________________________________________________________________________

<img width="1907" height="884" alt="Captura de tela 2026-04-06 104326" src="https://github.com/user-attachments/assets/85365016-99f0-47c7-8af7-40a5e19b667f" />
_______________________________________________________________________________________________________________________________________________________
<img width="1905" height="542" alt="Captura de tela 2026-04-06 104336" src="https://github.com/user-attachments/assets/3c9586fb-70b9-4071-8e77-d4e698bafadc" />
_______________________________________________________________________________________________________________________________________________________

<img width="1902" height="532" alt="Captura de tela 2026-04-06 104347" src="https://github.com/user-attachments/assets/3ecb4182-7c3a-4de7-9280-1068772651bd" />
_______________________________________________________________________________________________________________________________________________________

<img width="1894" height="448" alt="Captura de tela 2026-04-06 104356" src="https://github.com/user-attachments/assets/d7a7d07a-49ce-47f1-9269-baf2b6e9b568" />
_______________________________________________________________________________________________________________________________________________________

<img width="1907" height="545" alt="Captura de tela 2026-04-06 104406" src="https://github.com/user-attachments/assets/7ec447d5-5d69-475f-8788-7b3b314cfb35" />
_______________________________________________________________________________________________________________________________________________________

<img width="1909" height="508" alt="Captura de tela 2026-04-06 104414" src="https://github.com/user-attachments/assets/9c961e67-9be9-41b5-be85-53dfad079080" />




