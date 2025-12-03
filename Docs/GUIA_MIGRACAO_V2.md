# 🔄 GUIA DE MIGRAÇÃO - API v1.0 → v2.0

> Guia completo para migração dos endpoints legados (CRUD genérico) para os novos endpoints baseados em casos de uso

## 📌 Visão Geral

A versão 2.0 da API Agilit introduz uma **arquitetura baseada em casos de uso**, onde cada controller representa uma funcionalidade específica do negócio. Os endpoints antigos foram mantidos para **compatibilidade**, mas recomendamos migrar para os novos endpoints.

### Benefícios da Migração

- ✅ **Endpoints mais específicos**: Cada endpoint tem um propósito claro
- ✅ **Melhor documentação**: Mapeamento direto com requisitos de negócio
- ✅ **Validações aprimoradas**: Regras de negócio mais robustas
- ✅ **Respostas mais ricas**: Informações contextuais adicionais
- ✅ **Código mais manutenível**: Facilita evolução e testes

---

## 🗺️ Mapa de Migração

### 🔐 Autenticação

#### Login Unificado (NOVO)

**Antes (v1.0):**
```bash
# Login de Credor
POST /api/credor/login
{
  "email": "joao@email.com",
  "senha": "senha123"
}

# Login de Devedor
POST /api/devedor/login
{
  "email": "maria@email.com",
  "senha": "senha123"
}
```

**Depois (v2.0):**
```bash
# Login Unificado
POST /api/auth/login
{
  "email": "joao@email.com",
  "senha": "senha123",
  "tipo": "CREDOR"  # ou "DEVEDOR"
}
```

**Vantagens:**
- ✅ Um único endpoint para ambos os tipos
- ✅ Resposta padronizada com campo `tipo`
- ✅ Facilita implementação de frontend

---

### 👤 CREDOR

#### UC-C01: Criar Conta

**Antes (v1.0):**
```bash
POST /api/credor
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senhaHash": "senha123",
  "saldoDisponivel": 10000.00
}
```

**Depois (v2.0):**
```bash
POST /api/credor/criar-conta
{
  "nome": "João Silva",
  "cpf": "12345678900",
  "email": "joao@email.com",
  "senhaHash": "senha123",
  "telefone": "(11) 98765-4321",
  "saldoDisponivel": 10000.00
}
```

**Mudanças:**
- ✅ Validação de email único
- ✅ Validação de CPF único
- ✅ CPF agora é obrigatório
- ✅ Telefone adicionado
- ✅ Hash de senha automático

**Verificações Disponíveis:**
```bash
# Verificar se email está disponível
GET /api/auth/verificar-email?email=joao@email.com&tipo=CREDOR

# Verificar se CPF está disponível
GET /api/auth/verificar-cpf?cpf=12345678900&tipo=CREDOR
```

---

#### UC-C02: Fazer Login

**Antes (v1.0):**
```bash
POST /api/credor/login
{
  "email": "joao@email.com",
  "senha": "senha123"
}
```

**Depois (v2.0 - Opção 1):**
```bash
POST /api/credor/login
{
  "email": "joao@email.com",
  "senha": "senha123"
}
```

**Depois (v2.0 - Opção 2 - RECOMENDADO):**
```bash
POST /api/auth/login
{
  "email": "joao@email.com",
  "senha": "senha123",
  "tipo": "CREDOR"
}
```

**Mudanças:**
- ✅ Resposta inclui campo `tipo: "CREDOR"`
- ✅ Mensagem de sucesso incluída
- ✅ Validação aprimorada

---

#### UC-C03: Criar Oferta de Empréstimo

**Antes (v1.0):**
```bash
POST /api/oferta
{
  "credor": { "id": 1 },
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 6,
  "parcelasMaximas": 24,
  "taxaJuros": 2.5,
  "diasAtePrimeiraCobranca": 30
}
```

**Depois (v2.0):**
```bash
POST /api/credor/criar-oferta
{
  "credor": { "id": 1 },
  "valorDisponivel": 5000.00,
  "parcelasMinimas": 6,
  "parcelasMaximas": 24,
  "taxaJuros": 2.5,
  "diasAtePrimeiraCobranca": 30
}
```

**Mudanças:**
- ✅ Validação de saldo disponível do credor
- ✅ Resposta mais detalhada
- ✅ Endpoints adicionais para listar ofertas

**Novos Endpoints:**
```bash
# Listar minhas ofertas
GET /api/credor/criar-oferta/minhas/{credorId}

# Listar apenas ofertas ativas
GET /api/credor/criar-oferta/minhas/{credorId}/ativas
```

---

#### UC-C04: Gerar Proposta de Empréstimo

**Antes (v1.0):**
```bash
POST /api/oferta/{ofertaId}/criar-proposta
{
  "valorSolicitado": 5000.00,
  "numeroParcelas": 12
}
```

**Depois (v2.0):**
```bash
POST /api/credor/gerar-proposta
{
  "ofertaId": 1,
  "taxaJuros": 2.5  # opcional
}
```

**Mudanças:**
- ✅ Endpoint mais intuitivo
- ✅ Copia todos os dados da oferta automaticamente
- ✅ Taxa de juros opcional (usa da oferta se não informada)
- ✅ Geração automática de ID público

**Novos Endpoints:**
```bash
# Listar minhas propostas
GET /api/credor/gerar-proposta/minhas/{credorId}

# Cancelar proposta
PUT /api/credor/gerar-proposta/{propostaId}/cancelar
```

---

#### UC-C05: Registrar Empréstimo

**Antes (v1.0):**
```bash
# Aprovar interesse
PUT /api/interesse/{interesseId}/aprovar

# Confirmar empréstimo
POST /api/interesse/{interesseId}/confirmar-credor
{
  "numeroParcelas": 12
}
```

**Depois (v2.0):**
```bash
# Aprovar interesse (mantido igual)
PUT /api/interesse/{interesseId}/aprovar

# Confirmar empréstimo (novo endpoint)
POST /api/credor/registrar-emprestimo/{interesseId}/confirmar
{
  "numeroParcelas": 12
}
```

**Mudanças:**
- ✅ Endpoint específico para o caso de uso
- ✅ Validação de saldo antes de confirmar
- ✅ Resposta indica se aguarda confirmação do devedor
- ✅ Criação automática do empréstimo após confirmação bilateral

**Novos Endpoints:**
```bash
# Listar interesses pendentes de confirmação
GET /api/credor/registrar-emprestimo/pendentes/{credorId}

# Listar meus empréstimos
GET /api/credor/registrar-emprestimo/meus/{credorId}
```

---

### 👥 DEVEDOR

#### UC-D01: Criar Conta

**Antes (v1.0):**
```bash
POST /api/devedor
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria@email.com",
  "senhaHash": "senha123",
  "telefone": "(11) 91234-5678"
}
```

**Depois (v2.0):**
```bash
POST /api/devedor/criar-conta
{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria@email.com",
  "senhaHash": "senha123",
  "telefone": "(11) 91234-5678",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567"
}
```

**Mudanças:**
- ✅ Validação de email único
- ✅ Validação de CPF único
- ✅ Dados de endereço opcionais no cadastro inicial
- ✅ Hash de senha automático

**Novo Endpoint:**
```bash
# Completar dados cadastrais posteriormente
PUT /api/devedor/criar-conta/{devedorId}/completar-dados
{
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "telefone": "(11) 91234-5678"
}
```

---

#### UC-D02: Fazer Login

**Antes (v1.0):**
```bash
POST /api/devedor/login
{
  "email": "maria@email.com",
  "senha": "senha123"
}
```

**Depois (v2.0 - Opção 1):**
```bash
POST /api/devedor/login
{
  "email": "maria@email.com",
  "senha": "senha123"
}
```

**Depois (v2.0 - Opção 2 - RECOMENDADO):**
```bash
POST /api/auth/login
{
  "email": "maria@email.com",
  "senha": "senha123",
  "tipo": "DEVEDOR"
}
```

**Mudanças:**
- ✅ Resposta inclui campo `dadosCompletos: true/false`
- ✅ Aviso se dados cadastrais incompletos
- ✅ Campo `tipo: "DEVEDOR"` na resposta

---

#### UC-D03: Buscar Propostas de Empréstimo

**Antes (v1.0):**
```bash
# Listar propostas públicas
GET /api/proposta/publicas

# Buscar por ID público
GET /api/proposta/publico/{idPublico}
```

**Depois (v2.0):**
```bash
# Listar propostas com filtros
GET /api/devedor/buscar-propostas?valorMin=1000&valorMax=5000&taxaJurosMax=3.0

# Buscar proposta específica
GET /api/devedor/buscar-propostas/{idPublico}

# Detalhes completos com simulações
GET /api/devedor/buscar-propostas/{idPublico}/detalhes
```

**Mudanças:**
- ✅ Filtros múltiplos disponíveis
- ✅ Endpoint de detalhes inclui simulações de parcelas
- ✅ Novos endpoints de busca especializada

**Novos Endpoints:**
```bash
# Buscar por faixa de valor
GET /api/devedor/buscar-propostas/faixa/{faixa}
# Faixas: ate-1000, 1000-5000, 5000-10000, acima-10000

# Propostas com menor taxa
GET /api/devedor/buscar-propostas/menor-taxa?limite=10

# Propostas mais recentes
GET /api/devedor/buscar-propostas/recentes?limite=10
```

---

#### UC-D04: Selecionar Proposta

**Antes (v1.0):**
```bash
POST /api/interesse
{
  "proposta": { "id": 1 },
  "devedor": { "id": 1 }
}
```

**Depois (v2.0):**
```bash
POST /api/devedor/selecionar-proposta
{
  "propostaId": 1,
  "devedorId": 1
}
```

**Mudanças:**
- ✅ Validação de dados completos do devedor
- ✅ Verificação de interesse duplicado
- ✅ Resposta inclui próximo passo
- ✅ Notificação automática ao credor

**Novos Endpoints:**
```bash
# Listar meus interesses
GET /api/devedor/selecionar-proposta/meus/{devedorId}

# Listar interesses pendentes
GET /api/devedor/selecionar-proposta/meus/{devedorId}/pendentes

# Listar interesses aprovados
GET /api/devedor/selecionar-proposta/meus/{devedorId}/aprovados

# Cancelar interesse
DELETE /api/devedor/selecionar-proposta/{interesseId}

# Detalhes do interesse
GET /api/devedor/selecionar-proposta/interesse/{interesseId}
```

---

#### UC-D05: Pedir Empréstimo

**Antes (v1.0):**
```bash
POST /api/interesse/{interesseId}/confirmar-devedor
{
  "numeroParcelas": 12
}
```

**Depois (v2.0):**
```bash
POST /api/devedor/pedir-emprestimo/{interesseId}/confirmar
{
  "numeroParcelas": 12
}
```

**Mudanças:**
- ✅ Endpoint específico para o caso de uso
- ✅ Resposta indica se aguarda confirmação do credor
- ✅ Criação automática do empréstimo após confirmação bilateral
- ✅ Retorna parcelas geradas quando empréstimo é criado

**Novos Endpoints:**
```bash
# Simular parcelas antes de confirmar
POST /api/devedor/pedir-emprestimo/{interesseId}/simular
{
  "numeroParcelas": 12
}

# Listar pedidos pendentes
GET /api/devedor/pedir-emprestimo/pendentes/{devedorId}

# Listar meus empréstimos
GET /api/devedor/pedir-emprestimo/meus/{devedorId}

# Detalhes do empréstimo
GET /api/devedor/pedir-emprestimo/emprestimo/{emprestimoId}
```

---

#### UC-D06: Aceitar Termos (NOVO)

**Antes (v1.0):**
```
Não existia
```

**Depois (v2.0):**
```bash
# Aceitar termos
POST /api/devedor/aceitar-termos/{devedorId}
{
  "aceitouTermos": true,
  "versaoTermos": "1.0",
  "ip": "192.168.1.1"  # opcional
}

# Obter termos atuais
GET /api/devedor/aceitar-termos/termos-atuais

# Verificar status de aceitação
GET /api/devedor/aceitar-termos/{devedorId}/status
```

**Funcionalidade Nova:**
- ✅ Registro de aceitação de termos com auditoria
- ✅ Controle de versão dos termos
- ✅ Registro de IP (opcional)
- ✅ Data/hora da aceitação

---

## 📋 Checklist de Migração

### Para Desenvolvedores Frontend

- [ ] Atualizar endpoint de login para `/api/auth/login`
- [ ] Adicionar campo `tipo` nas requisições de login
- [ ] Atualizar endpoints de criação de conta
- [ ] Implementar verificação de email/CPF antes do cadastro
- [ ] Atualizar endpoints de ofertas e propostas
- [ ] Implementar novos endpoints de busca de propostas
- [ ] Adicionar funcionalidade de aceitar termos
- [ ] Atualizar fluxo de confirmação bilateral
- [ ] Implementar simulação de parcelas
- [ ] Testar todos os fluxos completos

### Para Desenvolvedores Backend

- [ ] Manter endpoints legados até migração completa
- [ ] Adicionar logs de uso dos endpoints legados
- [ ] Monitorar uso dos endpoints antigos
- [ ] Planejar deprecação dos endpoints legados
- [ ] Criar testes para novos endpoints
- [ ] Atualizar documentação Swagger
- [ ] Criar scripts de migração de dados (se necessário)

---

## 🔄 Estratégia de Migração Recomendada

### Fase 1: Preparação (1-2 semanas)
1. Estudar nova documentação
2. Identificar todos os pontos de integração
3. Criar ambiente de testes
4. Testar novos endpoints

### Fase 2: Migração Gradual (2-4 semanas)
1. Migrar autenticação primeiro
2. Migrar criação de contas
3. Migrar fluxo de ofertas/propostas
4. Migrar fluxo de interesses/empréstimos
5. Adicionar funcionalidade de termos

### Fase 3: Validação (1-2 semanas)
1. Testes de integração completos
2. Testes de carga
3. Validação com usuários beta
4. Correção de bugs

### Fase 4: Finalização (1 semana)
1. Deploy em produção
2. Monitoramento intensivo
3. Suporte aos usuários
4. Documentação de lições aprendidas

---

## 🆘 Problemas Comuns e Soluções

### Problema 1: Email já cadastrado

**Erro:**
```json
{
  "erro": "Email já cadastrado"
}
```

**Solução:**
Use o endpoint de verificação antes de criar conta:
```bash
GET /api/auth/verificar-email?email=joao@email.com&tipo=CREDOR
```

---

### Problema 2: Dados incompletos do devedor

**Erro:**
```json
{
  "erro": "Complete seus dados cadastrais (endereço, cidade, estado, CEP) antes de selecionar uma proposta"
}
```

**Solução:**
Complete os dados usando:
```bash
PUT /api/devedor/criar-conta/{devedorId}/completar-dados
```

---

### Problema 3: Interesse duplicado

**Erro:**
```json
{
  "erro": "Você já demonstrou interesse nesta proposta"
}
```

**Solução:**
Verifique seus interesses existentes:
```bash
GET /api/devedor/selecionar-proposta/meus/{devedorId}
```

---

### Problema 4: Saldo insuficiente

**Erro:**
```json
{
  "erro": "Saldo insuficiente. Disponível: R$ 3000.00, Necessário: R$ 5000.00"
}
```

**Solução:**
Registre saldo adicional ou crie oferta com valor menor.

---

## 📊 Comparação de Respostas

### Exemplo: Login

**v1.0:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "saldoDisponivel": 10000.00
}
```

**v2.0:**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "cpf": "12345678900",
  "telefone": "(11) 98765-4321",
  "saldoDisponivel": 10000.00,
  "tipo": "CREDOR",
  "mensagem": "Login realizado com sucesso"
}
```

**Diferenças:**
- ✅ Mais campos retornados
- ✅ Campo `tipo` identifica o usuário
- ✅ Mensagem de sucesso incluída

---

## 🎯 Endpoints Mantidos (Compatibilidade)

Os seguintes endpoints legados foram **mantidos** para compatibilidade:

### CRUD Genérico
- `/api/credor` - CRUD de credores
- `/api/devedor` - CRUD de devedores
- `/api/oferta` - CRUD de ofertas
- `/api/proposta` - CRUD de propostas
- `/api/interesse` - CRUD de interesses
- `/api/emprestimo` - CRUD de empréstimos
- `/api/parcela` - CRUD de parcelas
- `/api/notificacao` - CRUD de notificações

**Recomendação:** Use os novos endpoints baseados em casos de uso para novas implementações.

---

## 📚 Documentação Adicional

### Para Referência Completa
- **API v2.0**: `Docs/API_DOCUMENTATION_COMPLETE_V2.md`
- **Exemplos**: `Docs/EXEMPLOS_REQUISICOES_V2.md`
- **Resumo**: `Docs/PROJECT_SUMMARY_V2.md`
- **Proposta**: `PROPOSTA_ESTRUTURA_CASOS_DE_USO.md`

### Para Entendimento
- **Casos de Uso**: `RESUMO_CONTROLLERS_CASOS_DE_USO.md`
- **Arquitetura**: `Docs/ARCHITECTURE_PLAN.md`
- **Relacionamentos**: `Docs/ENTITY_RELATIONSHIPS.md`

---

## 💡 Dicas Finais

1. **Teste em ambiente de desenvolvimento primeiro**
2. **Migre um fluxo por vez**
3. **Mantenha logs detalhados durante a migração**
4. **Comunique mudanças aos usuários**
5. **Tenha um plano de rollback**
6. **Monitore performance após migração**
7. **Colete feedback dos usuários**

---

## 📞 Suporte

Para dúvidas sobre a migração:
1. Consulte a documentação completa em `Docs/`
2. Revise os exemplos em `EXEMPLOS_REQUISICOES_V2.md`
3. Teste os endpoints em ambiente de desenvolvimento

---

**Versão:** 1.0  
**Data:** 03/12/2024  
**Autor:** Equipe Agilit - Sistema de Empréstimos