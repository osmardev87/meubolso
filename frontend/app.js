
const API_BASE_URL = "http://localhost:8080";

const $ = (id) => document.getElementById(id);

const loginView = $("loginView");
const accountsView = $("accountsView");
const loginForm = $("loginForm");
const loginButton = $("loginButton");
const message = $("message");
const accountForm = $("accountForm");
const accountMessage = $("accountMessage");
const saveAccountButton = $("saveAccountButton");
const accountsList = $("accountsList");

const formatCurrency = (value) =>
    Number(value ?? 0).toLocaleString("pt-BR", {
        style: "currency",
        currency: "BRL"
    });

function showMessage(element, text, success = false) {
    element.textContent = text;
    element.style.color = success ? "#34d399" : "#f87171";
}

function showLogin() {
    loginView.hidden = false;
    accountsView.hidden = true;
}

function showAccounts() {
    loginView.hidden = true;
    accountsView.hidden = false;
}

async function apiRequest(path, options = {}) {
    const token = sessionStorage.getItem("meuBolsoToken");

    const response = await fetch(`${API_BASE_URL}${path}`, {
        ...options,
        headers: {
            ...(options.body ? { "Content-Type": "application/json" } : {}),
            ...(token ? { Authorization: `Bearer ${token}` } : {}),
            ...options.headers
        }
    });

    if (response.status === 401) {
        sessionStorage.removeItem("meuBolsoToken");
        showLogin();
        throw new Error("Sessão expirada ou não autorizada. Entre novamente.");
    }

    if (!response.ok) {
        const body = await response.text();
        let detail = body;

        try {
            const parsed = JSON.parse(body);
            detail = parsed.message || parsed.detail || body;
        } catch {
            // Mantém a mensagem original quando a resposta não é JSON.
        }

        throw new Error(detail || `Erro HTTP ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

// LOGIN
loginForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    loginButton.disabled = true;
    loginButton.textContent = "Entrando...";
    message.textContent = "";

    try {
        const data = await apiRequest("/api/auth/login", {
            method: "POST",
            body: JSON.stringify({
                email: $("email").value.trim(),
                password: $("password").value
            })
        });

        if (!data?.token) {
            throw new Error("A API não retornou o token JWT.");
        }

        sessionStorage.setItem("meuBolsoToken", data.token);

        showAccounts();
        await loadAccounts();
    } catch (error) {
        showMessage(message, error.message);
    } finally {
        loginButton.disabled = false;
        loginButton.textContent = "Entrar";
    }
});

// LISTAR CONTAS
async function loadAccounts() {
    accountsList.replaceChildren();

    const loading = document.createElement("p");
    loading.className = "empty-state";
    loading.textContent = "Carregando contas...";
    accountsList.appendChild(loading);

    try {
        const contas = await apiRequest("/api/contas");
        renderAccounts(Array.isArray(contas) ? contas : []);
    } catch (error) {
        accountsList.replaceChildren();

        const errorMessage = document.createElement("p");
        errorMessage.className = "empty-state";
        errorMessage.textContent = error.message;
        accountsList.appendChild(errorMessage);
    }
}

function renderAccounts(contas) {
    accountsList.replaceChildren();

    const total = contas.reduce(
        (sum, conta) => sum + Number(conta.saldoInicial ?? 0),
        0
    );

    $("totalBalance").textContent = formatCurrency(total);

    if (contas.length === 0) {
        const empty = document.createElement("p");
        empty.className = "empty-state";
        empty.textContent = "Nenhuma conta cadastrada. Adicione sua primeira conta!";
        accountsList.appendChild(empty);
        return;
    }

    contas.forEach((conta) => {
        const card = document.createElement("article");
        card.className = "account-card";

        const info = document.createElement("div");
        info.className = "account-info";

        const name = document.createElement("h3");
        name.textContent = conta.nome;

        const type = document.createElement("p");
        type.textContent = String(conta.tipo)
            .replaceAll("_", " ")
            .toLowerCase()
            .replace(/^\p{L}/u, (letter) => letter.toUpperCase());

        info.append(name, type);

        const balance = document.createElement("strong");
        balance.className = "account-balance";
        balance.textContent = formatCurrency(conta.saldoInicial);

        card.append(info, balance);
        accountsList.appendChild(card);
    });
}

// CADASTRAR CONTA
accountForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    saveAccountButton.disabled = true;
    saveAccountButton.textContent = "Salvando...";
    accountMessage.textContent = "";

    const saldoInicial = Number($("initialBalance").value);

    if (!Number.isFinite(saldoInicial) || saldoInicial < 0) {
        showMessage(accountMessage, "Informe um saldo inicial válido.");
        saveAccountButton.disabled = false;
        saveAccountButton.textContent = "Salvar conta";
        return;
    }

    try {
        await apiRequest("/api/contas", {
            method: "POST",
            body: JSON.stringify({
                nome: $("accountName").value.trim(),
                tipo: $("accountType").value,
                saldoInicial
            })
        });

        accountForm.reset();
        $("initialBalance").value = "0.00";

        showMessage(accountMessage, "Conta cadastrada com sucesso!", true);
        await loadAccounts();
    } catch (error) {
        showMessage(accountMessage, error.message);
    } finally {
        saveAccountButton.disabled = false;
        saveAccountButton.textContent = "Salvar conta";
    }
});

// SAIR
$("logoutButton").addEventListener("click", () => {
    sessionStorage.removeItem("meuBolsoToken");
    loginForm.reset();
    accountForm.reset();
    showLogin();
});

// RESTAURAR A SESSÃO DESTA ABA
if (sessionStorage.getItem("meuBolsoToken")) {
    showAccounts();
    loadAccounts();
} else {
    showLogin();
}