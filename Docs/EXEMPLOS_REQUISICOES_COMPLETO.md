# 🚀 EXEMPLOS COMPLETOS DE REQUISIÇÕES - API Agilit

## 📌 Base URL
```
http://localhost:8080/Sistemas_Distriubidos_Agilit/api
```

---

## 👤 CREDOR

### 1. Criar Credor (POST /api/credor)

**Exemplo de Dados:**
```json
{
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao.silva@example.com",
  "senhaHash": "senha123",
  "saldoDisponivel": 10000.00
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678900",
    "telefone": "(11) 98765-4321",
    "email": "joao.silva@example.com",
    "senhaHash": "senha123",
    "saldoDisponivel": 10000.00
  }'
```

**Resposta Esperada (201):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao.silva@example.com",
  "senhaHash": "senha123",
  "saldoDisponivel": 10000.00
}
```

---

### 2. Criar Conta de Credor com Validação (POST /api/credor/criar-conta)

**Exemplo de Dados:**
```json
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "telefone": "(11) 91234-5678",
  "email": "maria.santos@example.com",
  "senhaHash": "senha456"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-conta \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Santos",
    "cpf": "98765432100",
    "telefone": "(11) 91234-5678",
    "email": "maria.santos@example.com",
    "senhaHash": "senha456"
  }'
```

**Resposta Esperada (201):**
```json
{
  "id": 2,
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "telefone": "(11) 91234-5678",
  "email": "maria.santos@example.com",
  "senhaHash": "senha456",
  "saldoDisponivel": 0.0
}
```

---

### 3. Listar Todos os Credores (GET /api/credor)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "cpf": "12345678900",
    "telefone": "(11) 98765-4321",
    "email": "joao.silva@example.com",
    "saldoDisponivel": 10000.00
  },
  {
    "id": 2,
    "nome": "Maria Santos",
    "cpf": "98765432100",
    "telefone": "(11) 91234-5678",
    "email": "maria.santos@example.com",
    "saldoDisponivel": 0.0
  }
]
```

---

### 4. Buscar Credor por ID (GET /api/credor/{id})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/1
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao.silva@example.com",
  "saldoDisponivel": 10000.00
}
```

---

### 5. Atualizar Credor (PUT /api/credor/{id})

**Exemplo de Dados:**
```json
{
  "nome": "João Silva Atualizado",
  "cpf": "12345678900",
  "telefone": "(11) 98765-9999",
  "email": "joao.novo@example.com",
  "saldoDisponivel": 15000.00
}
```

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva Atualizado",
    "cpf": "12345678900",
    "telefone": "(11) 98765-9999",
    "email": "joao.novo@example.com",
    "saldoDisponivel": 15000.00
  }'
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "nome": "João Silva Atualizado",
  "cpf": "12345678900",
  "telefone": "(11) 98765-9999",
  "email": "joao.novo@example.com",
  "saldoDisponivel": 15000.00
}
```

---

### 6. Deletar Credor (DELETE /api/credor/{id})

**cURL:**
```bash
curl -X DELETE http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/1
```

**Resposta Esperada (204 No Content)**

---

### 7. Registrar Saldo (PUT /api/credor/saldo/{id})

**Exemplo de Dados:**
```json
{
  "valor": 5000.00
}
```

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/saldo/1 \
  -H "Content-Type: application/json" \
  -d '{
    "valor": 5000.00
  }'
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "email": "joao.silva@example.com",
  "saldoDisponivel": 15000.00
}
```

---

## 👥 DEVEDOR

### 8. Criar Devedor (POST /api/devedor)

**Exemplo de Dados:**
```json
{
  "nome": "Carlos Oliveira",
  "cpf": "11122233344",
  "telefone": "(11) 99999-8888",
  "email": "carlos.oliveira@example.com",
  "senhaHash": "senha789",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dataNascimento": "1990-05-15",
  "credor": {
    "id": 1
  }
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carlos Oliveira",
    "cpf": "11122233344",
    "telefone": "(11) 99999-8888",
    "email": "carlos.oliveira@example.com",
    "senhaHash": "senha789",
    "endereco": "Rua das Flores, 123",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567",
    "dataNascimento": "1990-05-15",
    "credor": {
      "id": 1
    }
  }'
```

**Resposta Esperada (201):**
```json
{
  "id": 1,
  "nome": "Carlos Oliveira",
  "cpf": "11122233344",
  "telefone": "(11) 99999-8888",
  "email": "carlos.oliveira@example.com",
  "senhaHash": "$2a$10$hashedpassword...",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dataNascimento": "1990-05-15"
}
```

---

### 9. Listar Todos os Devedores (GET /api/devedor)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "nome": "Carlos Oliveira",
    "cpf": "11122233344",
    "telefone": "(11) 99999-8888",
    "email": "carlos.oliveira@example.com",
    "endereco": "Rua das Flores, 123",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567",
    "dataNascimento": "1990-05-15"
  }
]
```

---

### 10. Buscar Devedor por ID (GET /api/devedor/{id})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor/1
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "nome": "Carlos Oliveira",
  "cpf": "11122233344",
  "telefone": "(11) 99999-8888",
  "email": "carlos.oliveira@example.com",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dataNascimento": "1990-05-15"
}
```

---

## 💰 OFERTA DE EMPRÉSTIMO

### 11. Criar Oferta (POST /api/oferta)

**Exemplo de Dados:**
```json
{
  "credor": {
    "id": 1
  },
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta \
  -H "Content-Type: application/json" \
  -d '{
    "credor": {
      "id": 1
    },
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5
  }'
```

**Resposta Esperada (201):**
```json
{
  "id": 1,
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:30:00",
  "ativa": true
}
```

---

### 12. Listar Ofertas do Credor (GET /api/oferta/credor/{credorId})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta/credor/1
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "dataCriacao": "2024-01-15T10:30:00",
    "ativa": true
  }
]
```

---

### 13. Listar Ofertas Ativas do Credor (GET /api/oferta/credor/{credorId}/ativas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta/credor/1/ativas
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "dataCriacao": "2024-01-15T10:30:00",
    "ativa": true
  }
]
```

---

### 14. Buscar Oferta por ID (GET /api/oferta/{id})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta/1
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:30:00",
  "ativa": true
}
```

---

### 15. Deletar Oferta (DELETE /api/oferta/{id})

**cURL:**
```bash
curl -X DELETE http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta/1
```

**Resposta Esperada (204 No Content)**

---

### 16. Criar Proposta a partir da Oferta (POST /api/oferta/{id}/criar-proposta)

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta/1/criar-proposta \
  -H "Content-Type: application/json"
```

**Resposta Esperada (201):**
```json
{
  "id": 1,
  "idPublico": "#ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:35:00",
  "status": "ATIVA"
}
```

---

### 17. Calcular Opções de Parcelas (GET /api/oferta/{id}/opcoes-parcelas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta/1/opcoes-parcelas
```

**Resposta Esperada (200):**
```json
[
  {
    "numeroParcelas": 3,
    "valorParcela": 1708.33,
    "valorTotal": 5125.00,
    "jurosTotal": 125.00
  },
  {
    "numeroParcelas": 6,
    "valorParcela": 875.00,
    "valorTotal": 5250.00,
    "jurosTotal": 250.00
  },
  {
    "numeroParcelas": 12,
    "valorParcela": 458.33,
    "valorTotal": 5500.00,
    "jurosTotal": 500.00
  }
]
```

---

## 📢 PROPOSTA DE EMPRÉSTIMO

### 18. Listar Propostas Públicas (GET /api/proposta/publicas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/publicas
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "dataCriacao": "2024-01-15T10:35:00",
    "status": "ATIVA"
  }
]
```

---

### 19. Listar Propostas do Credor (GET /api/proposta/credor/{credorId})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/credor/1
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "dataCriacao": "2024-01-15T10:35:00",
    "status": "ATIVA"
  }
]
```

---

### 20. Buscar Proposta por ID Público (GET /api/proposta/publico/{idPublico})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/publico/%23ABC123
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "idPublico": "#ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:35:00",
  "status": "ATIVA"
}
```

---

### 21. Buscar Proposta por ID (GET /api/proposta/{id})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/1
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "idPublico": "#ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:35:00",
  "status": "ATIVA"
}
```

---

### 22. Cancelar Proposta (PUT /api/proposta/{id}/cancelar)

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/1/cancelar \
  -H "Content-Type: application/json"
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "idPublico": "#ABC123",
  "nomeCredor": "João Silva",
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 3,
  "parcelasMaximas": 12,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "dataCriacao": "2024-01-15T10:35:00",
  "status": "CANCELADA"
}
```

---

### 23. Obter Detalhes da Proposta (GET /api/proposta/{id}/detalhes)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/1/detalhes
```

**Resposta Esperada (200):**
```json
{
  "proposta": {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5,
    "status": "ATIVA"
  },
  "opcoesParcelas": [
    {
      "numeroParcelas": 3,
      "valorParcela": 1708.33,
      "valorTotal": 5125.00,
      "jurosTotal": 125.00
    }
  ],
  "dataPrimeiraParcela": "2024-02-14",
  "totalInteresses": 2
}
```

---

### 24. Filtrar Propostas por Status (GET /api/proposta/status/{status})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/status/ATIVA
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "status": "ATIVA"
  }
]
```

---

### 25. Buscar Propostas por Valor (GET /api/proposta/buscar)

**cURL:**
```bash
curl -X GET "http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/buscar?valorMin=1000&valorMax=10000"
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "idPublico": "#ABC123",
    "nomeCredor": "João Silva",
    "valorDisponivel": 5000.00,
    "status": "ATIVA"
  }
]
```

---

### 26. Obter Estatísticas da Proposta (GET /api/proposta/{id}/estatisticas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/proposta/1/estatisticas
```

**Resposta Esperada (200):**
```json
{
  "totalInteresses": 5,
  "interessesPendentes": 2,
  "interessesAprovados": 2,
  "emprestimosGerados": 1,
  "status": "ATIVA"
}
```

---

## ❤️ INTERESSE EM PROPOSTA

### 27. Demonstrar Interesse (POST /api/interesse)

**Exemplo de Dados:**
```json
{
  "proposta": {
    "id": 1
  },
  "devedor": {
    "id": 1
  },
  "mensagem": "Tenho interesse nesta proposta de empréstimo"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse \
  -H "Content-Type: application/json" \
  -d '{
    "proposta": {
      "id": 1
    },
    "devedor": {
      "id": 1
    },
    "mensagem": "Tenho interesse nesta proposta de empréstimo"
  }'
```

**Resposta Esperada (201):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "PENDENTE",
  "mensagem": "Tenho interesse nesta proposta de empréstimo",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": false
}
```

---

### 28. Listar Interessados na Proposta (GET /api/interesse/proposta/{propostaId})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/proposta/1
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "dataInteresse": "2024-01-15T11:00:00",
    "status": "PENDENTE",
    "mensagem": "Tenho interesse nesta proposta de empréstimo",
    "confirmacaoCredor": false,
    "confirmacaoDevedor": false
  }
]
```

---

### 29. Listar Meus Interesses (GET /api/interesse/devedor/{devedorId})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/devedor/1
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "dataInteresse": "2024-01-15T11:00:00",
    "status": "PENDENTE",
    "mensagem": "Tenho interesse nesta proposta de empréstimo",
    "confirmacaoCredor": false,
    "confirmacaoDevedor": false
  }
]
```

---

### 30. Aprovar Interesse (PUT /api/interesse/{id}/aprovar)

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1/aprovar \
  -H "Content-Type: application/json"
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "APROVADO",
  "mensagem": "Tenho interesse nesta proposta de empréstimo",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": false
}
```

---

### 31. Rejeitar Interesse (PUT /api/interesse/{id}/rejeitar)

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1/rejeitar \
  -H "Content-Type: application/json"
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "REJEITADO",
  "mensagem": "Tenho interesse nesta proposta de empréstimo",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": false
}
```

---

### 32. Credor Confirma Empréstimo (POST /api/interesse/{id}/confirmar-credor)

**Exemplo de Dados:**
```json
{
  "numeroParcelas": 6
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1/confirmar-credor \
  -H "Content-Type: application/json" \
  -d '{
    "numeroParcelas": 6
  }'
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "APROVADO",
  "confirmacaoCredor": true,
  "confirmacaoDevedor": false,
  "dataConfirmacaoCredor": "2024-01-15T14:00:00"
}
```

---

### 33. Devedor Confirma Empréstimo (POST /api/interesse/{id}/confirmar-devedor)

**Exemplo de Dados:**
```json
{
  "numeroParcelas": 6
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1/confirmar-devedor \
  -H "Content-Type: application/json" \
  -d '{
    "numeroParcelas": 6
  }'
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "APROVADO",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": true,
  "dataConfirmacaoDevedor": "2024-01-15T15:00:00"
}
```

---

### 34. Cancelar Interesse (DELETE /api/interesse/{id})

**cURL:**
```bash
curl -X DELETE http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1
```

**Resposta Esperada (204 No Content)**

---

## 💳 EMPRÉSTIMO

### 35. Listar Todos os Empréstimos (GET /api/emprestimo)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/emprestimo
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "valorPrincipal": 5000.00,
    "jurosAplicados": 250.00,
    "valorTotal": 5250.00,
    "numeroParcelas": 6,
    "parcelasPagas": 2,
    "dataInicio": "2024-01-15",
    "dataVencimento": "2024-07-15",
    "status": "EM_ANDAMENTO"
  }
]
```

---

### 36. Buscar Empréstimo por ID (GET /api/emprestimo/{id})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/emprestimo/1
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "numeroParcelas": 6,
  "parcelasPagas": 2,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

---

### 37. Criar Empréstimo (POST /api/emprestimo)

**Exemplo de Dados:**
```json
{
  "credor": {
    "id": 1
  },
  "devedor": {
    "id": 1
  },
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "numeroParcelas": 6,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/emprestimo \
  -H "Content-Type: application/json" \
  -d '{
    "credor": {
      "id": 1
    },
    "devedor": {
      "id": 1
    },
    "valorPrincipal": 5000.00,
    "jurosAplicados": 250.00,
    "valorTotal": 5250.00,
    "numeroParcelas": 6,
    "dataInicio": "2024-01-15",
    "dataVencimento": "2024-07-15",
    "status": "EM_ANDAMENTO"
  }'
```

**Resposta Esperada (201):**
```json
{
  "id": 1,
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "numeroParcelas": 6,
  "parcelasPagas": 0,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

---

### 38. Atualizar Empréstimo (PUT /api/emprestimo/{id})

**Exemplo de Dados:**
```json
{
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/emprestimo/1 \
  -H "Content-Type: application/json" \
  -d '{
    "valorPrincipal": 5000.00,
    "jurosAplicados": 250.00,
    "valorTotal": 5250.00,
    "dataInicio": "2024-01-15",
    "dataVencimento": "2024-07-15",
    "status": "EM_ANDAMENTO"
  }'
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "valorPrincipal": 5000.00,
  "jurosAplicados": 250.00,
  "valorTotal": 5250.00,
  "numeroParcelas": 6,
  "parcelasPagas": 2,
  "dataInicio": "2024-01-15",
  "dataVencimento": "2024-07-15",
  "status": "EM_ANDAMENTO"
}
```

---

### 39. Deletar Empréstimo (DELETE /api/emprestimo/{id})

**cURL:**
```bash
curl -X DELETE http://localhost:8080/Sistemas_Distriubidos_Agilit/api/emprestimo/1
```

**Resposta Esperada (204 No Content)**

---

## 📅 PARCELA

### 40. Listar Parcelas do Empréstimo (GET /api/parcela/emprestimo/{emprestimoId})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/emprestimo/1
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "numeroParcela": 1,
    "valor": 875.00,
    "dataVencimento": "2024-02-14",
    "dataPagamento": "2024-02-14",
    "paga": true,
    "atrasada": false
  },
  {
    "id": 2,
    "numeroParcela": 2,
    "valor": 875.00,
    "dataVencimento": "2024-03-14",
    "dataPagamento": null,
    "paga": false,
    "atrasada": false
  }
]
```

---

### 41. Buscar Parcela por ID (GET /api/parcela/{id})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/1
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "numeroParcela": 1,
  "valor": 875.00,
  "dataVencimento": "2024-02-14",
  "dataPagamento": "2024-02-14",
  "paga": true,
  "atrasada": false
}
```

---

### 42. Marcar Parcela como Paga (PUT /api/parcela/{id}/pagar)

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/2/pagar \
  -H "Content-Type: application/json"
```

**Resposta Esperada (200):**
```json
{
  "id": 2,
  "numeroParcela": 2,
  "valor": 875.00,
  "dataVencimento": "2024-03-14",
  "dataPagamento": "2024-03-14",
  "paga": true,
  "atrasada": false
}
```

---

### 43. Listar Parcelas Pendentes (GET /api/parcela/emprestimo/{emprestimoId}/pendentes)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/emprestimo/1/pendentes
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 2,
    "numeroParcela": 2,
    "valor": 875.00,
    "dataVencimento": "2024-03-14",
    "paga": false,
    "atrasada": false
  }
]
```

---

### 44. Listar Parcelas Pagas (GET /api/parcela/emprestimo/{emprestimoId}/pagas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/emprestimo/1/pagas
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "numeroParcela": 1,
    "valor": 875.00,
    "dataVencimento": "2024-02-14",
    "dataPagamento": "2024-02-14",
    "paga": true,
    "atrasada": false
  }
]
```

---

### 45. Listar Parcelas Atrasadas (GET /api/parcela/emprestimo/{emprestimoId}/atrasadas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/emprestimo/1/atrasadas
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 3,
    "numeroParcela": 3,
    "valor": 875.00,
    "dataVencimento": "2024-01-14",
    "paga": false,
    "atrasada": true
  }
]
```

---

### 46. Obter Resumo das Parcelas (GET /api/parcela/emprestimo/{emprestimoId}/resumo)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/emprestimo/1/resumo
```

**Resposta Esperada (200):**
```json
{
  "emprestimoId": 1,
  "valorTotal": 5250.00,
  "totalPago": 1750.00,
  "totalPendente": 3500.00,
  "numeroParcelas": 6,
  "parcelasPagas": 2,
  "parcelasPendentes": 4,
  "parcelasAtrasadas": 1,
  "status": "ATRASADO",
  "proximaParcela": {
    "id": 3,
    "numeroParcela": 3,
    "valor": 875.00,
    "dataVencimento": "2024-04-14"
  },
  "percentualPago": 33.33
}
```

---

### 47. Listar Todas as Parcelas Vencidas (GET /api/parcela/vencidas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/vencidas
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 3,
    "numeroParcela": 3,
    "valor": 875.00,
    "dataVencimento": "2024-01-14",
    "paga": false,
    "atrasada": true
  }
]
```

---

### 48. Listar Parcelas Próximas do Vencimento (GET /api/parcela/emprestimo/{emprestimoId}/proximas-vencimento)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/emprestimo/1/proximas-vencimento
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 4,
    "numeroParcela": 4,
    "valor": 875.00,
    "dataVencimento": "2024-01-20",
    "paga": false,
    "atrasada": false
  }
]
```

---

## 🔔 NOTIFICAÇÃO

### 49. Listar Notificações do Usuário (GET /api/notificacao/{tipoDestinatario}/{destinatarioId})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/CREDOR/1
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "tipoDestinatario": "CREDOR",
    "destinatarioId": 1,
    "tipo": "NOVO_INTERESSE",
    "titulo": "Novo Interesse",
    "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
    "lida": false,
    "dataCriacao": "2024-01-15T11:00:00",
    "dataLeitura": null,
    "referencia": "1",
    "tipoReferencia": "INTERESSE"
  }
]
```

---

### 50. Listar Notificações Não Lidas (GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/nao-lidas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/CREDOR/1/nao-lidas
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "tipoDestinatario": "CREDOR",
    "destinatarioId": 1,
    "tipo": "NOVO_INTERESSE",
    "titulo": "Novo Interesse",
    "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
    "lida": false,
    "dataCriacao": "2024-01-15T11:00:00"
  }
]
```

---

### 51. Contar Notificações Não Lidas (GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/count-nao-lidas)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/CREDOR/1/count-nao-lidas
```

**Resposta Esperada (200):**
```json
5
```

---

### 52. Buscar Notificação por ID (GET /api/notificacao/{id})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/1
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "tipoDestinatario": "CREDOR",
  "destinatarioId": 1,
  "tipo": "NOVO_INTERESSE",
  "titulo": "Novo Interesse",
  "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
  "lida": false,
  "dataCriacao": "2024-01-15T11:00:00"
}
```

---

### 53. Marcar Notificação como Lida (PUT /api/notificacao/{id}/marcar-lida)

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/1/marcar-lida \
  -H "Content-Type: application/json"
```

**Resposta Esperada (200):**
```json
{
  "id": 1,
  "tipoDestinatario": "CREDOR",
  "destinatarioId": 1,
  "tipo": "NOVO_INTERESSE",
  "titulo": "Novo Interesse",
  "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
  "lida": true,
  "dataCriacao": "2024-01-15T11:00:00",
  "dataLeitura": "2024-01-15T12:00:00"
}
```

---

### 54. Marcar Todas as Notificações como Lidas (PUT /api/notificacao/{tipoDestinatario}/{destinatarioId}/marcar-todas-lidas)

**cURL:**
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/CREDOR/1/marcar-todas-lidas \
  -H "Content-Type: application/json"
```

**Resposta Esperada (200):**
```json
{
  "marcadas": 5
}
```

---

### 55. Deletar Notificação (DELETE /api/notificacao/{id})

**cURL:**
```bash
curl -X DELETE http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/1
```

**Resposta Esperada (204 No Content)**

---

### 56. Deletar Notificações Lidas (DELETE /api/notificacao/{tipoDestinatario}/{destinatarioId}/lidas)

**cURL:**
```bash
curl -X DELETE http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/CREDOR/1/lidas
```

**Resposta Esperada (200):**
```json
{
  "deletadas": 3
}
```

---

### 57. Filtrar Notificações por Tipo (GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/tipo/{tipo})

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/CREDOR/1/tipo/NOVO_INTERESSE
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "tipo": "NOVO_INTERESSE",
    "titulo": "Novo Interesse",
    "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
    "lida": false,
    "dataCriacao": "2024-01-15T11:00:00"
  }
]
```

---

### 58. Listar Notificações Recentes (GET /api/notificacao/{tipoDestinatario}/{destinatarioId}/recentes)

**cURL:**
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/notificacao/CREDOR/1/recentes
```

**Resposta Esperada (200):**
```json
[
  {
    "id": 1,
    "tipo": "NOVO_INTERESSE",
    "titulo": "Novo Interesse",
    "mensagem": "Carlos Oliveira demonstrou interesse na proposta #ABC123",
    "lida": false,
    "dataCriacao": "2024-01-15T11:00:00"
  }
]
```

---

## 🔄 FLUXO COMPLETO COM EXEMPLOS

### Cenário: Credor empresta R$ 5.000 para Devedor

#### Passo 1: Criar Credor
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/criar-conta \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "cpf": "12345678900",
    "telefone": "(11) 98765-4321",
    "email": "joao.silva@example.com"
  }'
```

#### Passo 2: Adicionar Saldo
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/credor/saldo/1 \
  -H "Content-Type: application/json" \
  -d '{"valor": 10000.00}'
```

#### Passo 3: Criar Oferta
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta \
  -H "Content-Type: application/json" \
  -d '{
    "credor": {"id": 1},
    "valorDisponivel": 5000.00,
    "parcelasMinimas": 3,
    "parcelasMaximas": 12,
    "diasAtePrimeiraCobranca": 30,
    "taxaJuros": 2.5
  }'
```

#### Passo 4: Publicar Proposta
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/oferta/1/criar-proposta
```

#### Passo 5: Criar Devedor
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/devedor \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Carlos Oliveira",
    "cpf": "11122233344",
    "telefone": "(11) 99999-8888",
    "email": "carlos.oliveira@example.com",
    "senhaHash": "senha789",
    "endereco": "Rua das Flores, 123",
    "cidade": "São Paulo",
    "estado": "SP",
    "cep": "01234-567",
    "dataNascimento": "1990-05-15",
    "credor": {"id": 1}
  }'
```

#### Passo 6: Devedor Demonstra Interesse
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse \
  -H "Content-Type: application/json" \
  -d '{
    "proposta": {"id": 1},
    "devedor": {"id": 1},
    "mensagem": "Tenho interesse nesta proposta"
  }'
```

#### Passo 7: Credor Aprova
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1/aprovar
```

#### Passo 8: Credor Confirma (6 parcelas)
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1/confirmar-credor \
  -H "Content-Type: application/json" \
  -d '{"numeroParcelas": 6}'
```

#### Passo 9: Devedor Confirma (6 parcelas)
```bash
curl -X POST http://localhost:8080/Sistemas_Distriubidos_Agilit/api/interesse/1/confirmar-devedor \
  -H "Content-Type: application/json" \
  -d '{"numeroParcelas": 6}'
```

#### Passo 10: Ver Empréstimo Criado
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/emprestimo/1
```

#### Passo 11: Ver Parcelas
```bash
curl -X GET http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/emprestimo/1
```

#### Passo 12: Pagar Primeira Parcela
```bash
curl -X PUT http://localhost:8080/Sistemas_Distriubidos_Agilit/api/parcela/1/pagar
```

---

## 📝 NOTAS IMPORTANTES

### Formatos de Data
- **LocalDate**: `YYYY-MM-DD` (ex: `2024-01-15`)
- **LocalDateTime**: `YYYY-MM-DDTHH:mm:ss` (ex: `2024-01-15T10:30:00`)

### Caracteres Especiais em URLs
- `#` deve ser codificado como `%23` em URLs
- Exemplo: `#ABC123` → `%23ABC123`

### Headers Obrigatórios
Todas as requisições POST/PUT devem incluir:
```
Content-Type: application/json
```

### Códigos de Status
- **200** - OK
- **201** - Created
- **204** - No Content
- **400** - Bad Request
- **404** - Not Found
- **409** - Conflict
- **500** - Internal Server Error

---

**Total de Exemplos:** 58 endpoints completos com cURL e dados de exemplo