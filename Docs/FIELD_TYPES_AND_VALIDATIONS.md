# 📋 TIPOS DE CAMPOS E VALIDAÇÕES DETALHADAS - API Agilit

## 🎯 Objetivo
Este documento detalha TODOS os tipos de campos, validações, constraints e regras de negócio para cada modelo da API.

---

## 📊 TABELA DE TIPOS DE DADOS

| Tipo Java | Tipo JSON | Formato | Exemplo |
|-----------|-----------|---------|---------|
| Long | number | Inteiro | `1`, `123`, `9999` |
| Integer | number | Inteiro | `1`, `6`, `12` |
| Double | number | Decimal | `5000.00`, `2.5`, `875.50` |
| String | string | Texto | `"João Silva"`, `"12345678900"` |
| Boolean | boolean | true/false | `true`, `false` |
| LocalDate | string | YYYY-MM-DD | `"2024-01-15"` |
| LocalDateTime | string | YYYY-MM-DDTHH:mm:ss | `"2024-01-15T10:30:00"` |

---

## 👤 CREDOR

### Estrutura Completa

```typescript
interface Credor {
  id: number;                    // Long - Auto-gerado
  nome: string;                  // String - Obrigatório
  cpf: string;                   // String - Obrigatório, Único
  telefone: string;              // String - Obrigatório
  email: string;                 // String - Obrigatório, Único
  senhaHash: string | null;      // String - Opcional
  saldoDisponivel: number;       // Double - Padrão: 0.0
}
```

### Validações Detalhadas

| Campo | Tipo | Obrigatório | Único | Validação | Padrão |
|-------|------|-------------|-------|-----------|--------|
| `id` | Long | ❌ (auto) | ✅ | Auto-incremento | - |
| `nome` | String | ✅ | ❌ | Qualquer texto | - |
| `cpf` | String | ✅ | ✅ | 11 dígitos numéricos | - |
| `telefone` | String | ✅ | ❌ | Formato livre | - |
| `email` | String | ✅ | ✅ | Formato de email válido | - |
| `senhaHash` | String | ❌ | ❌ | Qualquer texto | null |
| `saldoDisponivel` | Double | ❌ | ❌ | >= 0 | 0.0 |

### Request Body - POST /api/credor

```json
{
  "nome": "string (obrigatório)",
  "cpf": "string (obrigatório, único, 11 dígitos)",
  "telefone": "string (obrigatório)",
  "email": "string (obrigatório, único, formato email)",
  "senhaHash": "string (opcional)",
  "saldoDisponivel": "number (opcional, padrão: 0.0)"
}
```

### Response Body - Sucesso (201)

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

### Regras de Negócio

1. **Email único**: Não pode haver dois credores com mesmo email
2. **CPF único**: Não pode haver dois credores com mesmo CPF
3. **Saldo inicial**: Se não informado, inicia com 0.0
4. **Senha**: Não é hasheada automaticamente neste endpoint

---

## 👥 DEVEDOR

### Estrutura Completa

```typescript
interface Devedor {
  id: number;                    // Long - Auto-gerado
  nome: string;                  // String - Obrigatório
  cpf: string;                   // String - Obrigatório, Único
  telefone: string;              // String - Obrigatório
  email: string;                 // String - Obrigatório
  senhaHash: string | null;      // String - Opcional (hasheada auto)
  endereco: string | null;       // String - Opcional
  cidade: string | null;         // String - Opcional
  estado: string | null;         // String - Opcional
  cep: string | null;            // String - Opcional
  dataNascimento: string | null; // LocalDate - Opcional
  credor: {                      // Objeto - Opcional
    id: number;                  // Long - ID do credor
  } | null;
}
```

### Validações Detalhadas

| Campo | Tipo | Obrigatório | Único | Validação | Padrão |
|-------|------|-------------|-------|-----------|--------|
| `id` | Long | ❌ (auto) | ✅ | Auto-incremento | - |
| `nome` | String | ✅ | ❌ | Qualquer texto | - |
| `cpf` | String | ✅ | ✅ | 11 dígitos numéricos | - |
| `telefone` | String | ✅ | ❌ | Formato livre | - |
| `email` | String | ✅ | ❌ | Formato de email | - |
| `senhaHash` | String | ❌ | ❌ | Hasheada automaticamente | null |
| `endereco` | String | ❌ | ❌ | Qualquer texto | null |
| `cidade` | String | ❌ | ❌ | Qualquer texto | null |
| `estado` | String | ❌ | ❌ | Sigla UF (ex: SP) | null |
| `cep` | String | ❌ | ❌ | Formato: 00000-000 | null |
| `dataNascimento` | LocalDate | ❌ | ❌ | YYYY-MM-DD | null |
| `credor.id` | Long | ❌ | ❌ | Deve existir no BD | null |

### Request Body - POST /api/devedor

```json
{
  "nome": "string (obrigatório)",
  "cpf": "string (obrigatório, único)",
  "telefone": "string (obrigatório)",
  "email": "string (obrigatório)",
  "senhaHash": "string (opcional, será hasheada)",
  "endereco": "string (opcional)",
  "cidade": "string (opcional)",
  "estado": "string (opcional)",
  "cep": "string (opcional)",
  "dataNascimento": "string YYYY-MM-DD (opcional)",
  "credor": {
    "id": "number (opcional, deve existir)"
  }
}
```

### Response Body - Sucesso (201)

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

### Regras de Negócio

1. **CPF único**: Não pode haver dois devedores com mesmo CPF
2. **Senha hasheada**: Senha é automaticamente hasheada usando BCrypt
3. **Credor associado**: Se informado, deve existir no banco
4. **Dados completos para interesse**: Para demonstrar interesse em proposta, deve ter: endereco, cidade, estado, cep

---

## 💰 OFERTA DE EMPRÉSTIMO

### Estrutura Completa

```typescript
interface OfertaEmprestimo {
  id: number;                    // Long - Auto-gerado
  credor: {                      // Objeto - Obrigatório
    id: number;                  // Long - ID do credor
  };
  valorDisponivel: number;       // Double - Obrigatório
  parcelasMinimas: number;       // Integer - Obrigatório
  parcelasMaximas: number;       // Integer - Obrigatório
  diasAtePrimeiraCobranca: number; // Integer - Obrigatório
  taxaJuros: number;             // Double - Obrigatório
  dataCriacao: string;           // LocalDateTime - Auto-gerado
  ativa: boolean;                // Boolean - Padrão: true
}
```

### Validações Detalhadas

| Campo | Tipo | Obrigatório | Validação | Padrão |
|-------|------|-------------|-----------|--------|
| `id` | Long | ❌ (auto) | Auto-incremento | - |
| `credor.id` | Long | ✅ | Deve existir no BD | - |
| `valorDisponivel` | Double | ✅ | > 0 | - |
| `parcelasMinimas` | Integer | ✅ | >= 1 | - |
| `parcelasMaximas` | Integer | ✅ | >= parcelasMinimas | - |
| `diasAtePrimeiraCobranca` | Integer | ✅ | >= 0 | - |
| `taxaJuros` | Double | ✅ | >= 0 (percentual) | - |
| `dataCriacao` | LocalDateTime | ❌ (auto) | Auto-gerado | now() |
| `ativa` | Boolean | ❌ | true/false | true |

### Request Body - POST /api/oferta

```json
{
  "credor": {
    "id": "number (obrigatório, deve existir)"
  },
  "valorDisponivel": "number (obrigatório, > 0)",
  "parcelasMinimas": "number (obrigatório, >= 1)",
  "parcelasMaximas": "number (obrigatório, >= parcelasMinimas)",
  "diasAtePrimeiraCobranca": "number (obrigatório, >= 0)",
  "taxaJuros": "number (obrigatório, >= 0, percentual ex: 2.5)"
}
```

### Response Body - Sucesso (201)

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

### Regras de Negócio

1. **Credor deve existir**: ID do credor deve ser válido
2. **Valor positivo**: valorDisponivel deve ser maior que zero
3. **Parcelas válidas**: parcelasMaximas >= parcelasMinimas >= 1
4. **Taxa de juros**: Valor percentual (ex: 2.5 = 2.5%)
5. **Dias até cobrança**: Pode ser 0 (cobrança imediata)
6. **Oferta privada**: Apenas o credor que criou pode visualizar
7. **Não pode deletar**: Se já tiver propostas criadas

---

## 📢 PROPOSTA DE EMPRÉSTIMO

### Estrutura Completa

```typescript
interface PropostaEmprestimo {
  id: number;                    // Long - Auto-gerado
  idPublico: string;             // String - Auto-gerado (#ABC123)
  ofertaOrigem: {                // Objeto - Referência
    id: number;
  };
  credor: {                      // Objeto - Referência
    id: number;
  };
  nomeCredor: string;            // String - Desnormalizado
  valorDisponivel: number;       // Double - Copiado da oferta
  parcelasMinimas: number;       // Integer - Copiado da oferta
  parcelasMaximas: number;       // Integer - Copiado da oferta
  diasAtePrimeiraCobranca: number; // Integer - Copiado da oferta
  taxaJuros: number;             // Double - Copiado da oferta
  dataCriacao: string;           // LocalDateTime - Auto-gerado
  status: string;                // String - ATIVA/CANCELADA/ACEITA
}
```

### Validações Detalhadas

| Campo | Tipo | Obrigatório | Validação | Padrão |
|-------|------|-------------|-----------|--------|
| `id` | Long | ❌ (auto) | Auto-incremento | - |
| `idPublico` | String | ❌ (auto) | Formato: #ABC123 | gerado |
| `ofertaOrigem.id` | Long | ✅ | Deve existir | - |
| `credor.id` | Long | ✅ | Copiado da oferta | - |
| `nomeCredor` | String | ✅ | Copiado do credor | - |
| `valorDisponivel` | Double | ✅ | Copiado da oferta | - |
| `parcelasMinimas` | Integer | ✅ | Copiado da oferta | - |
| `parcelasMaximas` | Integer | ✅ | Copiado da oferta | - |
| `diasAtePrimeiraCobranca` | Integer | ✅ | Copiado da oferta | - |
| `taxaJuros` | Double | ✅ | Copiado da oferta | - |
| `dataCriacao` | LocalDateTime | ❌ (auto) | Auto-gerado | now() |
| `status` | String | ❌ | ATIVA/CANCELADA/ACEITA | ATIVA |

### Request Body - POST /api/oferta/{id}/criar-proposta

**Não requer body** - Todos os dados são copiados da oferta

### Response Body - Sucesso (201)

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

### Status Válidos

| Status | Descrição |
|--------|-----------|
| `ATIVA` | Proposta disponível para devedores |
| `CANCELADA` | Proposta cancelada pelo credor |
| `ACEITA` | Proposta aceita, empréstimo criado |

### Regras de Negócio

1. **ID público único**: Gerado automaticamente no formato #ABC123
2. **Proposta pública**: Visível para todos os devedores
3. **Dados copiados**: Todos os dados vêm da oferta original
4. **Nome desnormalizado**: Nome do credor é copiado para performance
5. **Cancelamento**: Só pode cancelar se não tiver interesses pendentes
6. **Status ACEITA**: Automaticamente quando empréstimo é criado

---

## ❤️ INTERESSE EM PROPOSTA

### Estrutura Completa

```typescript
interface InteresseProposta {
  id: number;                    // Long - Auto-gerado
  proposta: {                    // Objeto - Obrigatório
    id: number;
  };
  devedor: {                     // Objeto - Obrigatório
    id: number;
  };
  dataInteresse: string;         // LocalDateTime - Auto-gerado
  status: string;                // String - PENDENTE/APROVADO/REJEITADO/CANCELADO
  mensagem: string | null;       // String - Opcional (max 500)
  confirmacaoCredor: boolean;    // Boolean - Padrão: false
  confirmacaoDevedor: boolean;   // Boolean - Padrão: false
  dataConfirmacaoCredor: string | null;   // LocalDateTime - Opcional
  dataConfirmacaoDevedor: string | null;  // LocalDateTime - Opcional
}
```

### Validações Detalhadas

| Campo | Tipo | Obrigatório | Validação | Padrão |
|-------|------|-------------|-----------|--------|
| `id` | Long | ❌ (auto) | Auto-incremento | - |
| `proposta.id` | Long | ✅ | Deve existir e estar ATIVA | - |
| `devedor.id` | Long | ✅ | Deve existir com dados completos | - |
| `dataInteresse` | LocalDateTime | ❌ (auto) | Auto-gerado | now() |
| `status` | String | ❌ | PENDENTE/APROVADO/REJEITADO/CANCELADO | PENDENTE |
| `mensagem` | String | ❌ | Max 500 caracteres | null |
| `confirmacaoCredor` | Boolean | ❌ | true/false | false |
| `confirmacaoDevedor` | Boolean | ❌ | true/false | false |
| `dataConfirmacaoCredor` | LocalDateTime | ❌ | Auto ao confirmar | null |
| `dataConfirmacaoDevedor` | LocalDateTime | ❌ | Auto ao confirmar | null |

### Request Body - POST /api/interesse

```json
{
  "proposta": {
    "id": "number (obrigatório, deve existir e estar ATIVA)"
  },
  "devedor": {
    "id": "number (obrigatório, deve ter dados completos)"
  },
  "mensagem": "string (opcional, max 500 caracteres)"
}
```

### Request Body - POST /api/interesse/{id}/confirmar-credor

```json
{
  "numeroParcelas": "number (obrigatório, entre parcelasMinimas e parcelasMaximas)"
}
```

### Request Body - POST /api/interesse/{id}/confirmar-devedor

```json
{
  "numeroParcelas": "number (obrigatório, entre parcelasMinimas e parcelasMaximas)"
}
```

### Response Body - Sucesso (201)

```json
{
  "id": 1,
  "dataInteresse": "2024-01-15T11:00:00",
  "status": "PENDENTE",
  "mensagem": "Tenho interesse nesta proposta",
  "confirmacaoCredor": false,
  "confirmacaoDevedor": false,
  "dataConfirmacaoCredor": null,
  "dataConfirmacaoDevedor": null
}
```

### Status Válidos

| Status | Descrição |
|--------|-----------|
| `PENDENTE` | Aguardando aprovação do credor |
| `APROVADO` | Aprovado pelo credor, aguardando confirmações |
| `REJEITADO` | Rejeitado pelo credor |
| `CANCELADO` | Cancelado pelo devedor |

### Regras de Negócio

1. **Proposta ativa**: Proposta deve estar com status ATIVA
2. **Devedor completo**: Devedor deve ter endereco, cidade, estado, cep
3. **Interesse único**: Devedor não pode ter interesse duplicado na mesma proposta
4. **Aprovação**: Apenas credor pode aprovar/rejeitar
5. **Confirmação bilateral**: Ambos (credor e devedor) devem confirmar
6. **Número de parcelas**: Deve estar entre parcelasMinimas e parcelasMaximas
7. **Saldo do credor**: Credor deve ter saldo >= valorDisponivel
8. **Criação automática**: Quando ambos confirmam, empréstimo é criado automaticamente

---

## 💳 EMPRÉSTIMO

### Estrutura Completa

```typescript
interface Emprestimo {
  id: number;                    // Long - Auto-gerado
  devedor: {                     // Objeto - Obrigatório
    id: number;
  };
  credor: {                      // Objeto - Obrigatório
    id: number;
  };
  propostaOrigem: {              // Objeto - Opcional
    id: number;
  } | null;
  interesseOrigem: {             // Objeto - Opcional
    id: number;
  } | null;
  valorPrincipal: number;        // Double - Obrigatório
  jurosAplicados: number;        // Double - Obrigatório
  valorTotal: number;            // Double - Obrigatório
  numeroParcelas: number;        // Integer - Obrigatório
  parcelasPagas: number;         // Integer - Padrão: 0
  dataInicio: string;            // LocalDate - Padrão: hoje
  dataVencimento: string;        // LocalDate - Obrigatório
  status: string;                // String - EM_ANDAMENTO/PAGO/ATRASADO
}
```

### Validações Detalhadas

| Campo | Tipo | Obrigatório | Validação | Padrão |
|-------|------|-------------|-----------|--------|
| `id` | Long | ❌ (auto) | Auto-incremento | - |
| `devedor.id` | Long | ✅ | Deve existir | - |
| `credor.id` | Long | ✅ | Deve existir | - |
| `propostaOrigem.id` | Long | ❌ | Se informado, deve existir | null |
| `interesseOrigem.id` | Long | ❌ | Se informado, deve existir | null |
| `valorPrincipal` | Double | ✅ | > 0 | - |
| `jurosAplicados` | Double | ✅ | >= 0 | - |
| `valorTotal` | Double | ✅ | = valorPrincipal + jurosAplicados | - |
| `numeroParcelas` | Integer | ✅ | >= 1 | - |
| `parcelasPagas` | Integer | ❌ | >= 0, <= numeroParcelas | 0 |
| `dataInicio` | LocalDate | ❌ | YYYY-MM-DD | hoje |
| `dataVencimento` | LocalDate | ✅ | YYYY-MM-DD, >= dataInicio | - |
| `status` | String | ❌ | EM_ANDAMENTO/PAGO/ATRASADO | EM_ANDAMENTO |

### Request Body - POST /api/emprestimo

```json
{
  "credor": {
    "id": "number (obrigatório, deve existir)"
  },
  "devedor": {
    "id": "number (obrigatório, deve existir)"
  },
  "valorPrincipal": "number (obrigatório, > 0)",
  "jurosAplicados": "number (obrigatório, >= 0)",
  "valorTotal": "number (obrigatório, = valorPrincipal + jurosAplicados)",
  "numeroParcelas": "number (obrigatório, >= 1)",
  "dataInicio": "string YYYY-MM-DD (opcional, padrão: hoje)",
  "dataVencimento": "string YYYY-MM-DD (obrigatório)",
  "status": "string (opcional, padrão: EM_ANDAMENTO)"
}
```

### Response Body - Sucesso (201)

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

### Status Válidos

| Status | Descrição |
|--------|-----------|
| `EM_ANDAMENTO` | Empréstimo ativo com parcelas pendentes |
| `PAGO` | Todas as parcelas foram pagas |
| `ATRASADO` | Possui pelo menos uma parcela atrasada |

### Regras de Negócio

1. **Criação automática**: Normalmente criado via confirmação bilateral
2. **Parcelas**: Parcelas são criadas automaticamente
3. **Saldo do credor**: Saldo é debitado automaticamente
4. **Status automático**: Atualizado conforme pagamento de parcelas
5. **Cálculo de juros**: Juros = valorPrincipal * (taxaJuros/100) * numeroParcelas
6. **Data vencimento**: Calculada automaticamente baseada em parcelas

---

## 📅 PARCELA

### Estrutura Completa

```typescript
interface Parcela {
  id: number;                    // Long - Auto-gerado
  emprestimo: {                  // Objeto - Obrigatório
    id: number;
  };
  numeroParcela: number;         // Integer - Obrigatório
  valor: number;                 // Double - Obrigatório
  dataVencimento: string;        // LocalDate - Obrigatório
  dataPagamento: string | null;  // LocalDate - Opcional
  paga: boolean;                 // Boolean - Padrão: false
  atrasada: boolean;             // Boolean - Padrão: false
}
```

### Validações Detalhadas

| Campo | Tipo | Obrigatório | Validação | Padrão |
|-------|------|-------------|-----------|--------|
| `id` | Long | ❌ (auto) | Auto-incremento | - |
| `emprestimo.id` | Long | ✅ | Deve existir | - |
| `numeroParcela` | Integer | ✅ | >= 1 | - |
| `valor` | Double | ✅ | > 0 | - |
| `dataVencimento` | LocalDate | ✅ | YYYY-MM-DD | - |
| `dataPagamento` | LocalDate | ❌ | YYYY-MM-DD | null |
| `paga` | Boolean | ❌ | true/false | false |
| `atrasada` | Boolean | ❌ | true/false (calculado) | false |

### Request Body - PUT /api/parcela/{id}/pagar

**Não requer body** - Apenas marca como paga

### Response Body - Sucesso (200)

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

### Regras de Negócio

1. **Criação automática**: Parcelas são criadas junto com empréstimo
2. **Valor calculado**: valorTotal / numeroParcelas
3. **Datas calculadas**: Baseadas em diasAtePrimeiraCobranca
4. **Atraso automático**: Campo atrasada é calculado automaticamente
5. **Pagamento**: Apenas credor pode marcar como paga
6. **Atualização de status**: Atualiza status do empréstimo
7. **Notificações**: Envia notificações ao pagar

---

## 🔔 NOTIFICAÇÃO

### Estrutura Completa

```typescript
interface Notificacao {
  id: number;                    // Long - Auto-gerado
  tipoDestinatario: string;      // String - CREDOR/DEVEDOR
  destinatarioId: number;        // Long - ID do usuário
  tipo: string;                  // String - Tipo da notificação
  titulo: string;                // String - Max 200 caracteres
  mensagem: string;              // String - Max 1000 caracteres
  lida: boolean;                 // Boolean - Padrão: false
  dataCriacao: string;           // LocalDateTime - Auto-gerado
  dataLeitura: string | null;    // LocalDateTime - Opcional
  referencia: string | null;     // String - Max 100 caracteres
  tipoReferencia: string | null; // String - Max 50 caracteres
}
```

### Validações Detalhadas

| Campo | Tipo | Obrigatório | Validação | Padrão |
|-------|------|-------------|-----------|--------|
| `id` | Long | ❌ (auto) | Auto-incremento | - |
| `tipoDestinatario` | String | ✅ | CREDOR ou DEVEDOR | - |
| `destinatarioId` | Long | ✅ | ID válido | - |
| `tipo` | String | ✅ | Ver tipos válidos abaixo | - |
| `titulo` | String | ✅ | Max 200 caracteres | - |
| `mensagem` | String | ✅ | Max 1000 caracteres | - |
| `lida` | Boolean | ❌ | true/false | false |
| `dataCriacao` | LocalDateTime | ❌ (auto) | Auto-gerado | now() |
| `dataLeitura` | LocalDateTime | ❌ | Auto ao marcar lida | null |
| `referencia` | String | ❌ | Max 100 caracteres | null |
| `tipoReferencia` | String | ❌ | Max 50 caracteres | null |

### Tipos de Notificação Válidos

| Tipo | Descrição | Destinatário |
|------|-----------|--------------|
| `NOVO_INTERESSE` | Novo interesse em proposta | CREDOR |
| `APROVACAO` | Interesse aprovado | DEVEDOR |
| `CONFIRMACAO` | Empréstimo confirmado | AMBOS |
| `VENCIMENTO` | Parcela próxima do vencimento | AMBOS |
| `ATRASO` | Parcela atrasada | AMBOS |
| `PAGAMENTO` | Parcela paga | CREDOR |
| `QUITACAO` | Empréstimo quitado | AMBOS |
| `REJEICAO` | Interesse rejeitado | DEVEDOR |
| `CONFIRMACAO_PENDENTE` | Aguardando confirmação | AMBOS |

### Request Body - Criação Manual (Não recomendado)

Notificações são criadas automaticamente pelo sistema.

### Response Body - GET (200)

```json
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
```

### Regras de Negócio

1. **Criação automática**: Sistema cria notificações automaticamente
2. **Tipo destinatário**: Deve ser CREDOR ou DEVEDOR
3. **Marcar como lida**: Atualiza dataLeitura automaticamente
4. **Referência**: Armazena ID da entidade relacionada
5. **Tipo referência**: Indica tipo da entidade (EMPRESTIMO, PROPOSTA, etc)

---

## 🔄 FLUXO DE DADOS COMPLETO

### 1. Credor Cria Conta
```
INPUT: nome, cpf, telefone, email
OUTPUT: id, saldoDisponivel=0.0
```

### 2. Credor Adiciona Saldo
```
INPUT: valor (Double > 0)
OUTPUT: saldoDisponivel atualizado
```

### 3. Credor Cria Oferta
```
INPUT: credor.id, valorDisponivel, parcelas, dias, taxaJuros
OUTPUT: oferta com id, dataCriacao, ativa=true
```

### 4. Credor Publica Proposta
```
INPUT: oferta.id
OUTPUT: proposta com idPublico gerado, status=ATIVA
```

### 5. Devedor Demonstra Interesse
```
INPUT: proposta.id, devedor.id, mensagem
VALIDAÇÃO: devedor com dados completos
OUTPUT: interesse com status=PENDENTE
EFEITO: notificação para credor
```

### 6. Credor Aprova Interesse
```
INPUT: interesse.id
OUTPUT: interesse com status=APROVADO
EFEITO: notificação para devedor
```

### 7. Confirmação Bilateral
```
INPUT: interesse.id, numeroParcelas
VALIDAÇÃO: parcelas entre min e max, saldo suficiente
OUTPUT: confirmacaoCredor=true ou confirmacaoDevedor=true
EFEITO: Se ambos confirmaram → cria empréstimo
```

### 8. Empréstimo Criado Automaticamente
```
CÁLCULOS:
- juros = valorPrincipal * (taxaJuros/100) * numeroParcelas
- valorTotal = valorPrincipal + juros
- valorParcela = valorTotal / numeroParcelas
- datas calculadas baseadas em diasAtePrimeiraCobranca

EFEITOS:
- Cria empréstimo
- Cria parcelas
- Debita saldo do credor
- Atualiza proposta para ACEITA
- Envia notificações
```

### 9. Pagamento de Parcela
```
INPUT: parcela.id
EFEITOS:
- paga=true
- dataPagamento=hoje
- parcelasPagas++ no empréstimo
- Atualiza status do empréstimo
- Envia notificação
- Se quitado → notificação especial
```

---

## 📊 RESUMO DE CONSTRAINTS

### Campos Únicos
- `Credor.cpf`
- `Credor.email`
- `Devedor.cpf`
- `PropostaEmprestimo.idPublico`

### Campos Obrigatórios (NOT NULL)
- Todos os campos marcados como `nullable = false` no JPA
- Ver tabelas de validação acima

### Valores Padrão
- `Credor.saldoDisponivel` = 0.0
- `OfertaEmprestimo.ativa` = true
- `OfertaEmprestimo.dataCriacao` = now()
- `PropostaEmprestimo.status` = "ATIVA"
- `PropostaEmprestimo.dataCriacao` = now()
- `InteresseProposta.status` = "PENDENTE"
- `InteresseProposta.confirmacaoCredor` = false
- `InteresseProposta.confirmacaoDevedor` = false
- `Emprestimo.status` = "EM_ANDAMENTO"
- `Emprestimo.parcelasPagas` = 0
- `Emprestimo.dataInicio` = hoje
- `Parcela.paga` = false
- `Parcela.atrasada` = false
- `Notificacao.lida` = false
- `Notificacao.dataCriacao` = now()

### Relacionamentos Obrigatórios
- `OfertaEmprestimo.credor`
- `PropostaEmprestimo.ofertaOrigem`
- `PropostaEmprestimo.credor`
- `InteresseProposta.proposta`
- `InteresseProposta.devedor`
- `Emprestimo.credor`
- `Emprestimo.devedor`
- `Parcela.emprestimo`

---

## ⚠️ ERROS COMUNS E SOLUÇÕES

### 1. Email/CPF Duplicado
**Erro**: `409 Conflict`
**Solução**: Use email/CPF único

### 2. Credor Não Encontrado
**Erro**: `404 Not Found`
**Solução**: Verifique se o ID do credor existe

### 3. Valor Inválido
**Erro**: `400 Bad Request - "Valor deve ser maior que zero"`
**Solução**: Envie valores positivos

### 4. Parcelas Inválidas
**Erro**: `400 Bad Request - "Parcelas máximas deve ser maior ou igual às mínimas"`
**Solução**: Ajuste os valores de parcelas

### 5. Devedor Sem Dados Completos
**Erro**: `400 Bad Request - "Devedor deve ter dados completos"`
**Solução**: Preencha endereco, cidade, estado, cep

### 6. Interesse Duplicado
**Erro**: `409 Conflict - "Você já demonstrou interesse nesta proposta"`
**Solução**: Cada devedor pode ter apenas um interesse por proposta

### 7. Saldo Insuficiente
**Erro**: `400 Bad Request - "Credor não possui saldo disponível suficiente"`
**Solução**: Adicione saldo antes de confirmar

### 8. Número de Parcelas Fora do Intervalo
**Erro**: `400 Bad Request - "Número de parcelas deve estar entre X e Y"`
**Solução**: Escolha número dentro do intervalo permitido

---

**Documentação completa de tipos e validações**
**Versão:** 1.0
**Data:** 2024-01-15