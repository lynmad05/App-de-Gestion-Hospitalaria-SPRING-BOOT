document.addEventListener('DOMContentLoaded', () => {
    // --- ELEMENTOS GLOBALES ---
    const loginContainer = document.getElementById('login-container');
    const appContainer = document.getElementById('app-container');
    const mainContent = document.getElementById('main-content');
    const modalTitle = document.getElementById('modalTitle');
    const modalBody = document.getElementById('modalBody');
    const formModal = new bootstrap.Modal(document.getElementById('formModal'));
    const btnLogout = document.getElementById('btn-logout');
    const loginForm = document.getElementById('login-form');
    const loginError = document.getElementById('login-error');

    const API_BASE_URL = '/api/v1';
    let jwtToken = null;

    // --- LÓGICA DE AUTENTICACIÓN ---
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        loginError.classList.add('d-none');
        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password })
            });
            if (!response.ok) throw new Error('Usuario o contraseña incorrectos.');
            const data = await response.json();
            jwtToken = data.token;
            localStorage.setItem('jwtToken', jwtToken);
            showApp();
        } catch (error) {
            loginError.textContent = error.message;
            loginError.classList.remove('d-none');
        }
    });

    btnLogout.addEventListener('click', () => {
        jwtToken = null;
        localStorage.removeItem('jwtToken');
        showLogin();
    });

    function showApp() { loginContainer.style.display = 'none'; appContainer.style.display = 'block'; loadPacientes(); }
    function showLogin() { appContainer.style.display = 'none'; loginContainer.style.display = 'block'; }

    const storedToken = localStorage.getItem('jwtToken');
    if (storedToken) { jwtToken = storedToken; showApp(); }
    else { showLogin(); }

    // --- FUNCIONES GENÉRICAS PARA API (CON TOKEN) ---
    async function fetchData(endpoint) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, { headers: { 'Authorization': `Bearer ${jwtToken}` } });
        if (response.status === 403) { showLogin(); throw new Error('Acceso denegado o sesión expirada.'); }
        if (!response.ok) throw new Error(`Error al cargar datos de ${endpoint}.`);
        return await response.json();
    }
    // ... (Aquí irían postData, putData, etc. si se necesitaran más)

    // --- NAVEGACIÓN (¡AHORA COMPLETA!) ---
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            navLinks.forEach(nav => nav.classList.remove('active'));
            e.target.classList.add('active');
            const targetId = e.target.id;
            if (targetId === 'nav-pacientes') loadPacientes();
            if (targetId === 'nav-medicos') loadMedicosEspecialidades();
            if (targetId === 'nav-citas') loadCitas();
            if (targetId === 'nav-hospitalizacion') loadHospitalizacion();
            if (targetId === 'nav-facturas') loadFacturas();
            if (targetId === 'nav-usuarios') loadUsuarios();
            if (targetId === 'nav-bitacora') loadBitacora();
        });
    });

    // --- IMPLEMENTACIÓN DE CADA MÓDULO ---

    async function loadPacientes() {
        try {
            const pacientes = await fetchData('/pacientes');
            mainContent.innerHTML = `
                <h2>Gestión de Pacientes</h2>
                <table class="table table-striped"><thead><tr><th>ID</th><th>DNI</th><th>Nombre</th><th>Correo</th><th>Estado</th></tr></thead>
                <tbody>${pacientes.map(p => `<tr><td>${p.idPaciente}</td><td>${p.dni}</td><td>${p.nombres} ${p.apellidos}</td><td>${p.correo}</td><td><span class="badge bg-${p.estado === 'Activo' ? 'success' : 'danger'}">${p.estado}</span></td></tr>`).join('')}</tbody></table>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    async function loadMedicosEspecialidades() {
        try {
            const [medicos, especialidades] = await Promise.all([fetchData('/medicos'), fetchData('/especialidades')]);
            mainContent.innerHTML = `
                <div class="row">
                    <div class="col-md-7">
                        <h2>Médicos</h2>
                        <table class="table table-striped"><thead><tr><th>ID</th><th>Colegiatura</th><th>Nombre</th><th>Especialidades</th></tr></thead>
                        <tbody>${medicos.map(m => `<tr><td>${m.idMedico}</td><td>${m.colegiatura}</td><td>${m.nombres} ${m.apellidos}</td><td>${m.especialidades.map(e => `<span class="badge bg-info">${e.nombre}</span>`).join(' ')}</td></tr>`).join('')}</tbody></table>
                    </div>
                    <div class="col-md-5">
                        <h2>Especialidades</h2>
                        <ul class="list-group">${especialidades.map(e => `<li class="list-group-item">${e.nombre}</li>`).join('')}</ul>
                    </div>
                </div>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    async function loadCitas() {
        try {
            const citas = await fetchData('/citas');
            mainContent.innerHTML = `
                <h2>Gestión de Citas</h2>
                <table class="table table-striped"><thead><tr><th>ID</th><th>Fecha/Hora</th><th>Paciente</th><th>Médico</th><th>Estado</th></tr></thead>
                <tbody>${citas.map(c => `<tr><td>${c.idCita}</td><td>${c.fecha} ${c.hora}</td><td>${c.paciente.nombres} ${c.paciente.apellidos}</td><td>${c.medico.nombres} ${c.medico.apellidos}</td><td><span class="badge bg-primary">${c.estado}</span></td></tr>`).join('')}</tbody></table>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    async function loadHospitalizacion() {
        try {
            const [hospitalizaciones, habitaciones] = await Promise.all([fetchData('/hospitalizaciones'), fetchData('/habitaciones')]);
            mainContent.innerHTML = `
                <div class="row">
                    <div class="col-md-7">
                        <h2>Registros de Hospitalización</h2>
                        <table class="table table-striped"><thead><tr><th>ID</th><th>Paciente</th><th>Habitación</th><th>F. Ingreso</th><th>Estado</th></tr></thead>
                        <tbody>${hospitalizaciones.map(h => `<tr><td>${h.idHosp}</td><td>${h.paciente.nombres}</td><td>${h.habitacion.numero}</td><td>${h.fechaIngreso}</td><td><span class="badge bg-warning text-dark">${h.estado}</span></td></tr>`).join('')}</tbody></table>
                    </div>
                    <div class="col-md-5">
                        <h2>Estado de Habitaciones</h2>
                        <ul class="list-group">${habitaciones.map(h => `<li class="list-group-item d-flex justify-content-between align-items-center">${h.numero} (${h.tipo})<span class="badge bg-${h.estado === 'Disponible' ? 'success' : 'danger'}">${h.estado}</span></li>`).join('')}</ul>
                    </div>
                </div>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    async function loadFacturas() {
        try {
            const facturas = await fetchData('/facturas');
            mainContent.innerHTML = `
                <h2>Gestión de Facturas</h2>
                <table class="table table-striped"><thead><tr><th>ID</th><th>Paciente</th><th>Fecha</th><th>Total</th><th>Estado</th></tr></thead>
                <tbody>${facturas.map(f => `<tr><td>${f.idFactura}</td><td>${f.paciente.nombres} ${f.paciente.apellidos}</td><td>${f.fechaEmision}</td><td>S/ ${f.total.toFixed(2)}</td><td><span class="badge bg-${f.estado === 'Pagado' ? 'success' : 'warning text-dark'}">${f.estado}</span></td></tr>`).join('')}</tbody></table>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    async function loadUsuarios() {
        try {
            const usuarios = await fetchData('/usuarios');
            mainContent.innerHTML = `
                <h2>Gestión de Usuarios</h2>
                <table class="table table-striped"><thead><tr><th>ID</th><th>Usuario</th><th>Rol</th></tr></thead>
                <tbody>${usuarios.map(u => `<tr><td>${u.idUsuario}</td><td>${u.nombreUsuario}</td><td><span class="badge bg-secondary">${u.rol}</span></td></tr>`).join('')}</tbody></table>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    async function loadBitacora() {
        try {
            const registros = await fetchData('/bitacora');
            mainContent.innerHTML = `
                <h2>Bitácora del Sistema</h2>
                <table class="table table-striped"><thead><tr><th>ID</th><th>Usuario</th><th>Acción</th><th>Fecha y Hora</th></tr></thead>
                <tbody>${registros.map(b => `<tr><td>${b.idBitacora}</td><td>${b.usuario ? b.usuario.nombreUsuario : 'Sistema'}</td><td>${b.accion}</td><td>${new Date(b.fechaHora).toLocaleString('es-PE')}</td></tr>`).join('')}</tbody></table>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

});