# 🚀 Guia Completo - Testando AGILIT LOAN com Insomnia

## 📥 Passo 1: Abrir o Insomnia

1. Abra o **Insomnia** na sua máquina
2. Você verá a tela inicial do Insomnia

---

## 📁 Passo 2: Criar uma Collection

1. Clique em **"Create"** ou **"New Collection"**
2. Nome da Collection: **AGILIT LOAN API**
3. Clique em **"Create"**

---

## 🎯 Passo 3: Configurar Base URL (Opcional mas Recomendado)

1. Clique no ícone de **engrenagem** (⚙️) ao lado da collection
2. Vá em **"Environment"** ou **"Base Environment"**
3. Adicione:
```json
{
  "base_url": "http://localhost:8086/api"
}
```
4. Salve

Agora você pode usar `{{ base_url }}` em todas as requisições!

---

## 📋 Passo 4: Criar Pastas para Organizar

Dentro da collection **AGILIT LOAN API**, crie estas pastas:

1. **Credor** (clique com botão direito na collection → New Folder)
2. **Devedor**
3. **Ofertas**
4. **Propostas**
5. **Interesses**
6. **Parcelas**
7. **Notificações**
8. **Empréstimos**

---

## 🔥 Passo 5: Criar Requisições - FLUXO COMPLETO

### 📁 Pasta: Credor

#### 1. Criar Credor
- **Método:** POST
- **URL:** `{{ base_url }}/credor/criar-conta`
- **Headers:** 
  - `Content-Type: application/json`
- **Body (JSON):**
```json
{
  "nome": "João Silva",
  "email": "joao@example.com",
  "cpf": "12345678900",
  "telefone": "11999999999"
}
```
- **Salvar como:** "1. Criar Credor"

#### 2. Listar Credores
- **Método:** GET
- **URL:** `{{ base_url }}/credor`
- **Salvar como:** "2. Listar Credores"

#### 3. Registrar Saldo
- **Método:** POST
- **URL:** `{{ base_url }}/credor/registrar-saldo`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "credorId": 1,
  "saldo": 50000.00
}
```
- **Salvar como:** "3. Registrar Saldo"

---

### 📁 Pasta: Devedor

#### 1. Criar Devedor
- **Método:** POST
- **URL:** `{{ base_url }}/devedor`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "nome": "Maria Santos",
  "email": "maria@example.com",
  "cpf": "98765432100",
  "telefone": "11988888888",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "estado": "SP",
  "cep": "01234-567",
  "dataNascimento": "1990-05-15"
}
```
- **Salvar como:** "1. Criar Devedor"

#### 2. Listar Devedores
- **Método:** GET
- **URL:** `{{ base_url }}/devedor`
- **Salvar como:** "2. Listar Devedores"

---

### 📁 Pasta: Ofertas

#### 1. Criar Oferta
- **Método:** POST
- **URL:** `{{ base_url }}/ofertas`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "credor": {
    "id": 1
  },
  "valorDisponivel": 10000.00,
  "parcelasMinimas": 6,
  "parcelasMaximas": 24,
  "diasAtePrimeiraCobranca": 30,
  "taxaJuros": 2.5,
  "ativa": true
}
```
- **Salvar como:** "1. Criar Oferta"

#### 2. Listar Ofertas do Credor
- **Método:** GET
- **URL:** `{{ base_url }}/ofertas/credor/1`
- **Salvar como:** "2. Listar Ofertas do Credor"

#### 3. Criar Proposta a partir da Oferta
- **Método:** POST
- **URL:** `{{ base_url }}/ofertas/1/criar-proposta`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "valorSolicitado": 5000.00,
  "numeroParcelas": 12
}
```
- **Salvar como:** "3. Criar Proposta"

#### 4. Ver Opções de Parcelas
- **Método:** GET
- **URL:** `{{ base_url }}/ofertas/1/opcoes-parcelas?valor=5000`
- **Salvar como:** "4. Ver Opções de Parcelas"

---

### 📁 Pasta: Propostas

#### 1. Listar Propostas Públicas
- **Método:** GET
- **URL:** `{{ base_url }}/propostas/publicas`
- **Salvar como:** "1. Listar Propostas Públicas"

#### 2. Buscar Proposta por ID Público
- **Método:** GET
- **URL:** `{{ base_url }}/propostas/publico/ABC123`
- **Nota:** Substitua ABC123 pelo ID público real
- **Salvar como:** "2. Buscar por ID Público"

#### 3. Listar Propostas do Credor
- **Método:** GET
- **URL:** `{{ base_url }}/propostas/credor/1`
- **Salvar como:** "3. Listar Propostas do Credor"

#### 4. Ver Detalhes da Proposta
- **Método:** GET
- **URL:** `{{ base_url }}/propostas/1/detalhes`
- **Salvar como:** "4. Ver Detalhes"

#### 5. Cancelar Proposta
- **Método:** PUT
- **URL:** `{{ base_url }}/propostas/1/cancelar`
- **Salvar como:** "5. Cancelar Proposta"

---

### 📁 Pasta: Interesses

#### 1. Demonstrar Interesse
- **Método:** POST
- **URL:** `{{ base_url }}/interesses`
- **Headers:** `Content-Type: application/json`
- **Body:**
```json
{
  "proposta": {
    "id": 1
  },
  "devedor": {
    "id": 1
  }
}
```
- **Salvar como:** "1. Demonstrar Interesse"

#### 2. Listar Interesses da Proposta
- **Método:** GET
- **URL:** `{{ base_url }}/interesses/proposta/1`
- **Salvar como:** "2. Listar Interesses da Proposta"

#### 3. Aprovar Interesse (Credor)
- **Método:** PUT
- **URL:** `{{ base_url }}/interesses/1/aprovar`
- **Salvar como:** "3. Aprovar Interesse"

#### 4. Confirmar - Credor
- **Método:** POST
- **URL:** `{{ base_url }}/interesses/1/confirmar-credor`
- **Salvar como:** "4. Confirmar Credor"

#### 5. Confirmar - Devedor
- **Método:** POST
- **URL:** `{{ base_url }}/interesses/1/confirmar-devedor`
- **Salvar como:** "5. Confirmar Devedor"
- **Nota:** Após ambas confirmações, o empréstimo é criado automaticamente!

#### 6. Rejeitar Interesse
- **Método:** PUT
- **URL:** `{{ base_url }}/interesses/1/rejeitar`
- **Salvar como:** "6. Rejeitar Interesse"

---

### 📁 Pasta: Empréstimos

#### 1. Listar Empréstimos do Devedor
- **Método:** GET
- **URL:** `{{ base_url }}/emprestimos/devedor/1`
- **Salvar como:** "1. Listar Empréstimos do Devedor"

#### 2. Listar Empréstimos do Credor
- **Método:** GET
- **URL:** `{{ base_url }}/emprestimos/credor/1`
- **Salvar como:** "2. Listar Empréstimos do Credor"

#### 3. Ver Detalhes do Empréstimo
- **Método:** GET
- **URL:** `{{ base_url }}/emprestimos/1`
- **Salvar como:** "3. Ver Detalhes"

---

### 📁 Pasta: Parcelas

#### 1. Listar Parcelas do Empréstimo
- **Método:** GET
- **URL:** `{{ base_url }}/parcelas/emprestimo/1`
- **Salvar como:** "1. Listar Parcelas"

#### 2. Ver Parcelas Pendentes
- **Método:** GET
- **URL:** `{{ base_url }}/parcelas/emprestimo/1/pendentes`
- **Salvar como:** "2. Parcelas Pendentes"

#### 3. Ver Parcelas Atrasadas
- **Método:** GET
- **URL:** `{{ base_url }}/parcelas/emprestimo/1/atrasadas`
- **Salvar como:** "3. Parcelas Atrasadas"

#### 4. Marcar Parcela como Paga
- **Método:** PUT
- **URL:** `{{ base_url }}/parcelas/1/pagar`
- **Salvar como:** "4. Marcar como Paga"

#### 5. Ver Resumo Financeiro
- **Método:** GET
- **URL:** `{{ base_url }}/parcelas/emprestimo/1/resumo`
- **Salvar como:** "5. Resumo Financeiro"

#### 6. Ver Próxima Parcela
- **Método:** GET
- **URL:** `{{ base_url }}/parcelas/emprestimo/1/proxima`
- **Salvar como:** "6. Próxima Parcela"

---

### 📁 Pasta: Notificações

#### 1. Listar Notificações do Credor
- **Método:** GET
- **URL:** `{{ base_url }}/notificacoes/CREDOR/1`
- **Salvar como:** "1. Notificações do Credor"

#### 2. Listar Notificações do Devedor
- **Método:** GET
- **URL:** `{{ base_url }}/notificacoes/DEVEDOR/1`
- **Salvar como:** "2. Notificações do Devedor"

#### 3. Notificações Não Lidas
- **Método:** GET
- **URL:** `{{ base_url }}/notificacoes/CREDOR/1/nao-lidas`
- **Salvar como:** "3. Não Lidas"

#### 4. Marcar como Lida
- **Método:** PUT
- **URL:** `{{ base_url }}/notificacoes/1/marcar-lida`
- **Salvar como:** "4. Marcar como Lida"

#### 5. Marcar Todas como Lidas
- **Método:** PUT
- **URL:** `{{ base_url }}/notificacoes/CREDOR/1/marcar-todas-lidas`
- **Salvar como:** "5. Marcar Todas como Lidas"

#### 6. Contar Não Lidas
- **Método:** GET
- **URL:** `{{ base_url }}/notificacoes/CREDOR/1/count-nao-lidas`
- **Salvar como:** "6. Contar Não Lidas"

---

## 🎯 Passo 6: Testar o Fluxo Completo

Execute as requisições **nesta ordem** para testar o fluxo completo:

### Sequência de Teste:

1. ✅ **Criar Credor** (Pasta Credor → 1)
2. ✅ **Criar Devedor** (Pasta Devedor → 1)
3. ✅ **Criar Oferta** (Pasta Ofertas → 1)
4. ✅ **Criar Proposta** (Pasta Ofertas → 3)
5. ✅ **Listar Propostas Públicas** (Pasta Propostas → 1)
6. ✅ **Demonstrar Interesse** (Pasta Interesses → 1)
7. ✅ **Aprovar Interesse** (Pasta Interesses → 3)
8. ✅ **Confirmar Credor** (Pasta Interesses → 4)
9. ✅ **Confirmar Devedor** (Pasta Interesses → 5)
   - 🎉 **Empréstimo criado automaticamente!**
10. ✅ **Listar Parcelas** (Pasta Parcelas → 1)
11. ✅ **Marcar Parcela como Paga** (Pasta Parcelas → 4)
12. ✅ **Ver Notificações** (Pasta Notificações → 1 e 2)

---

## 💡 Dicas do Insomnia

### Usar Variáveis:
Depois de criar um Credor, copie o ID da resposta e use em outras requisições.

### Ver Histórico:
O Insomnia salva todas as respostas. Clique em "Timeline" para ver.

### Organizar:
Use cores diferentes para cada pasta (clique com botão direito → Color).

### Exportar:
Você pode exportar toda a collection (File → Export) para compartilhar.

---

## 🎓 Para Apresentar ao Professor

1. **Mostre a organização** - Collection bem estruturada em pastas
2. **Execute o fluxo completo** - Do início ao fim
3. **Mostre as respostas JSON** - Dados sendo criados e retornados
4. **Demonstre as notificações** - Sistema funcionando
5. **Mostre o controle de parcelas** - Pagamentos sendo registrados

---

## ✅ Checklist Final

- [ ] Insomnia instalado e aberto
- [ ] Collection "AGILIT LOAN API" criada
- [ ] Base URL configurada
- [ ] 8 pastas criadas
- [ ] Todas as requisições adicionadas
- [ ] Servidor rodando (`mvn jetty:run`)
- [ ] Fluxo completo testado
- [ ] Pronto para apresentar!

---

**Sua API está pronta para ser testada e apresentada! 🚀**