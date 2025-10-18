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

    function showApp() {
        loginContainer.style.display = 'none';
        appContainer.style.display = 'block';
        loadHome();
    }
    function showLogin() { appContainer.style.display = 'none'; loginContainer.style.display = 'block'; }

    const storedToken = localStorage.getItem('jwtToken');
    if (storedToken) { jwtToken = storedToken; showApp(); }
    else { showLogin(); }

    // --- FUNCIONES GENÉRICAS PARA API (CON TOKEN) ---
    async function fetchData(endpoint) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, { headers: { 'Authorization': `Bearer ${jwtToken}` } });
        if (response.status === 403 || response.status === 401) { showLogin(); throw new Error('Acceso denegado o sesión expirada.'); }
        if (!response.ok) throw new Error(`Error al cargar datos de ${endpoint}.`);
        return await response.json();
    }

    async function postData(endpoint, data) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json', 'Authorization': `Bearer ${jwtToken}`},
            body: JSON.stringify(data)
        });
        if (response.status === 403 || response.status === 401) { showLogin(); throw new Error('Acceso denegado o sesión expirada.'); }
        if (!response.ok) throw new Error(`Error al enviar datos a ${endpoint}.`);
        return await response.json();
    }

    async function putData(endpoint, data = {}) {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${jwtToken}`
            },
            body: JSON.stringify(data)
        });
        if (response.status === 403 || response.status === 401) { showLogin(); throw new Error('Acceso denegado o sesión expirada.'); }
        if (!response.ok) throw new Error(`Error al actualizar datos en ${endpoint}.`);
        return await response.json();
    }

    // --- NAVEGACIÓN (COMPLETA) ---
    const navLinks = document.querySelectorAll('.nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            navLinks.forEach(nav => nav.classList.remove('active'));
            e.target.classList.add('active');
            const targetId = e.target.id;
            if (targetId === 'nav-inicio') loadHome();
            if (targetId === 'nav-pacientes') loadPacientes();
            if (targetId === 'nav-medicos') loadMedicosEspecialidades();
            if (targetId === 'nav-citas') loadCitas();
            if (targetId === 'nav-hospitalizacion') loadHospitalizacion();
            if (targetId === 'nav-facturas') loadFacturas();
            if (targetId === 'nav-usuarios') loadUsuarios();
            if (targetId === 'nav-bitacora') loadBitacora();
        });
    });

    // --- PÁGINA DE INICIO ---
    async function loadHome() {
        mainContent.innerHTML = `
            <div class="jumbotron bg-light p-5 rounded-3 border">
                <h1 class="display-4">Bienvenido al Sistema de Gestión Hospitalaria</h1>
                <p class="lead">Gestione pacientes, citas, médicos y más, todo desde un solo lugar.</p>
                <hr class="my-4">
                <p>Seleccione un módulo de la barra de navegación para comenzar a trabajar.</p>
            </div>
            
            <div class="row text-center mt-5">
                <div class="col-md-4">
                    <div class="card shadow-sm mb-3">
                        <div class="card-body">
                            <i class="bi bi-people-fill" style="font-size: 3rem; color: var(--bs-primary);"></i>
                            <h5 class="card-title mt-3">Pacientes</h5>
                            <p class="card-text">Registrar y consultar expedientes.</p>
                            <button class="btn btn-primary" onclick="document.getElementById('nav-pacientes').click()">Ir a Pacientes</button>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card shadow-sm mb-3">
                        <div class="card-body">
                            <i class="bi bi-calendar-plus" style="font-size: 3rem; color: var(--bs-success);"></i>
                            <h5 class="card-title mt-3">Citas Médicas</h5>
                            <p class="card-text">Agendar y administrar citas.</p>
                            <button class="btn btn-success" onclick="document.getElementById('nav-citas').click()">Ir a Citas</button>
                        </div>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card shadow-sm mb-3">
                        <div class="card-body">
                            <i class="bi bi-file-earmark-text" style="font-size: 3rem; color: var(--bs-danger);"></i>
                            <h5 class="card-title mt-3">Facturación</h5>
                            <p class="card-text">Generar y controlar facturas.</p>
                            <button class="btn btn-danger" onclick="document.getElementById('nav-facturas').click()">Ir a Facturas</button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    // --- MÓDULO PACIENTES ---
    async function loadPacientes() {
        try {
            const pacientes = await fetchData('/pacientes');
            mainContent.innerHTML = `
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h2>Gestión de Pacientes</h2>
                    <button class="btn btn-primary" id="btn-add-paciente"><i class="bi bi-plus-circle"></i> Registrar Paciente</button>
                </div>
                <table class="table table-striped table-hover"><thead><tr><th>ID</th><th>DNI</th><th>Nombre</th><th>Correo</th><th>Estado</th></tr></thead>
                <tbody>${pacientes.map(p => `<tr><td>${p.idPaciente}</td><td>${p.dni}</td><td>${p.nombres} ${p.apellidos}</td><td>${p.correo}</td><td><span class="badge bg-${p.estado === 'Activo' ? 'success' : 'danger'}">${p.estado}</span></td></tr>`).join('')}</tbody></table>`;
            document.getElementById('btn-add-paciente').addEventListener('click', showPacienteForm);
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    function showPacienteForm() {
        modalTitle.textContent = 'Registrar Nuevo Paciente';
        modalBody.innerHTML = `
            <form id="paciente-form">
                <div class="row">
                    <div class="col-md-6 mb-3"><label for="dni" class="form-label">DNI</label><input type="text" class="form-control" id="dni" required></div>
                    <div class="col-md-6 mb-3"><label for="fechaNacimiento" class="form-label">Fecha de Nacimiento</label><input type="date" class="form-control" id="fechaNacimiento" required></div>
                </div>
                 <div class="row">
                    <div class="col-md-6 mb-3"><label for="nombres" class="form-label">Nombres</label><input type="text" class="form-control" id="nombres" required></div>
                    <div class="col-md-6 mb-3"><label for="apellidos" class="form-label">Apellidos</label><input type="text" class="form-control" id="apellidos" required></div>
                </div>
                 <div class="row">
                    <div class="col-md-6 mb-3"><label for="correo" class="form-label">Correo Electrónico</label><input type="email" class="form-control" id="correo" required></div>
                     <div class="col-md-6 mb-3"><label for="telefono" class="form-label">Teléfono</label><input type="text" class="form-control" id="telefono"></div>
                </div>
                 <div class="mb-3"><label for="direccion" class="form-label">Dirección</label><input type="text" class="form-control" id="direccion"></div>
                 <div class="mb-3"><label for="sexo" class="form-label">Sexo</label><select class="form-select" id="sexo"><option value="Masculino">Masculino</option><option value="Femenino">Femenino</option><option value="Otro">Otro</option></select></div>
                <div class="d-flex justify-content-end">
                    <button type="button" class="btn btn-secondary me-2" data-bs-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Guardar Paciente</button>
                </div>
            </form>
        `;
        formModal.show();
        document.getElementById('paciente-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const data = {
                dni: document.getElementById('dni').value,
                nombres: document.getElementById('nombres').value,
                apellidos: document.getElementById('apellidos').value,
                fechaNacimiento: document.getElementById('fechaNacimiento').value,
                sexo: document.getElementById('sexo').value,
                direccion: document.getElementById('direccion').value,
                telefono: document.getElementById('telefono').value,
                correo: document.getElementById('correo').value
            };
            try {
                await postData('/pacientes', data);
                formModal.hide();
                loadPacientes();
                alert('¡Paciente registrado con éxito!');
            } catch (error) { alert(error.message); }
        });
    }

    // --- MÓDULO MÉDICOS ---
    async function loadMedicosEspecialidades() {
        try {
            const [medicos, especialidades] = await Promise.all([fetchData('/medicos'), fetchData('/especialidades')]);
            mainContent.innerHTML = `
                <div class="row">
                    <div class="col-md-7">
                        <h2>Médicos</h2>
                        <table class="table table-striped table-hover"><thead><tr><th>ID</th><th>Colegiatura</th><th>Nombre</th><th>Especialidades</th></tr></thead>
                        <tbody>${medicos.map(m => `<tr><td>${m.idMedico}</td><td>${m.colegiatura}</td><td>${m.nombres} ${m.apellidos}</td><td>${m.especialidades.map(e => `<span class="badge bg-info">${e.nombre}</span>`).join(' ')}</td></tr>`).join('')}</tbody></table>
                    </div>
                    <div class="col-md-5">
                        <h2>Especialidades</h2>
                        <ul class="list-group">${especialidades.map(e => `<li class="list-group-item">${e.nombre}</li>`).join('')}</ul>
                    </div>
                </div>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    // --- MÓDULO CITAS (¡CORREGIDO!) ---
    async function loadCitas() {
        try {
            const citas = await fetchData('/citas');
            mainContent.innerHTML = `
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h2>Gestión de Citas</h2>
                    <button class="btn btn-primary" id="btn-add-cita"><i class="bi bi-calendar-plus"></i> Agendar Cita</button>
                </div>
                <table class="table table-striped table-hover">
                    <thead><tr><th>ID</th><th>Fecha/Hora</th><th>Paciente</th><th>Médico</th><th>Estado</th><th>Acciones</th></tr></thead>
                    <tbody>
                        ${citas.map(c => `
                            <tr>
                                <td>${c.idCita}</td>
                                <td>${new Date(c.fecha + 'T' + c.hora).toLocaleString('es-PE')}</td>
                                <td>${c.paciente.nombres} ${c.paciente.apellidos}</td>
                                <td>${c.medico.nombres} ${c.medico.apellidos}</td>
                                <td><span class="badge ${getBadgeClass(c.estado)}">${c.estado}</span></td>
                                <td>
                                    ${c.estado === 'Programada' ? `
                                        <button class="btn btn-sm btn-warning btn-reprogramar" data-id="${c.idCita}" title="Reprogramar"><i class="bi bi-pencil-square"></i></button>
                                        <button class="btn btn-sm btn-danger btn-cancelar" data-id="${c.idCita}" title="Cancelar"><i class="bi bi-x-circle"></i></button>
                                    ` : ''}
                                </td>
                            </tr>`).join('')}
                    </tbody>
                </table>`;

            document.querySelectorAll('.btn-cancelar').forEach(btn => {
                btn.addEventListener('click', async (e) => {
                    const id = e.currentTarget.dataset.id;
                    if (confirm('¿Está seguro de que desea CANCELAR esta cita?')) {
                        try {
                            await putData(`/citas/${id}/cancelar`);
                            loadCitas();
                        } catch (error) { alert('Error al cancelar la cita: ' + error.message); }
                    }
                });
            });

            document.querySelectorAll('.btn-reprogramar').forEach(btn => {
                btn.addEventListener('click', () => alert('Funcionalidad de reprogramar aún no implementada.'));
            });

            // ¡ESTA LÍNEA ES LA CORRECCIÓN!
            document.getElementById('btn-add-cita').addEventListener('click', showCitaForm);
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    // ¡NUEVA FUNCIÓN PARA EL FORMULARIO DE CITAS!
    async function showCitaForm() {
        modalTitle.textContent = 'Agendar Nueva Cita';
        try {
            // Obtenemos pacientes y médicos para los dropdowns
            const [pacientes, medicos] = await Promise.all([
                fetchData('/pacientes'),
                fetchData('/medicos')
            ]);

            modalBody.innerHTML = `
                <form id="cita-form">
                    <div class="mb-3">
                        <label for="paciente" class="form-label">Paciente</label>
                        <select class="form-select" id="paciente" required>
                            <option value="">Seleccione un paciente...</option>
                            ${pacientes.map(p => `<option value="${p.idPaciente}">${p.nombres} ${p.apellidos}</option>`).join('')}
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="medico" class="form-label">Médico</label>
                        <select class="form-select" id="medico" required>
                            <option value="">Seleccione un médico...</option>
                            ${medicos.map(m => `<option value="${m.idMedico}">${m.nombres} ${m.apellidos} (${m.especialidades.map(e => e.nombre).join(', ')})</option>`).join('')}
                        </select>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3"><label for="fecha" class="form-label">Fecha</label><input type="date" class="form-control" id="fecha" required></div>
                        <div class="col-md-6 mb-3"><label for="hora" class="form-label">Hora</label><input type="time" class="form-control" id="hora" required></div>
                    </div>
                    <div class="mb-3"><label for="motivo" class="form-label">Motivo de la Cita</label><textarea class="form-control" id="motivo" rows="3" required></textarea></div>
                    <div class="d-flex justify-content-end">
                        <button type="button" class="btn btn-secondary me-2" data-bs-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-primary">Agendar Cita</button>
                    </div>
                </form>
            `;
            formModal.show();

            document.getElementById('cita-form').addEventListener('submit', async (e) => {
                e.preventDefault();
                const data = {
                    paciente: { idPaciente: document.getElementById('paciente').value },
                    medico: { idMedico: document.getElementById('medico').value },
                    fecha: document.getElementById('fecha').value,
                    hora: document.getElementById('hora').value,
                    motivo: document.getElementById('motivo').value
                };
                try {
                    await postData('/citas', data);
                    formModal.hide();
                    loadCitas();
                    alert('¡Cita agendada con éxito!');
                } catch (error) { alert(error.message); }
            });

        } catch(error) {
            alert('No se pudieron cargar los datos para el formulario de citas. ' + error.message);
        }
    }

    function getBadgeClass(estado) {
        switch(estado) {
            case 'Programada': return 'bg-primary';
            case 'Atendida': return 'bg-success';
            case 'Cancelada': return 'bg-danger';
            default: return 'bg-secondary';
        }
    }

    // --- MÓDULO HOSPITALIZACIÓN ---
    async function loadHospitalizacion() {
        try {
            const [hospitalizaciones, habitaciones] = await Promise.all([fetchData('/hospitalizaciones'), fetchData('/habitaciones')]);
            mainContent.innerHTML = `
                <div class="row">
                    <div class="col-md-7">
                        <h2>Registros de Hospitalización</h2>
                        <table class="table table-striped table-hover"><thead><tr><th>ID</th><th>Paciente</th><th>Habitación</th><th>F. Ingreso</th><th>Estado</th></tr></thead>
                        <tbody>${hospitalizaciones.map(h => `<tr><td>${h.idHosp}</td><td>${h.paciente.nombres}</td><td>${h.habitacion.numero}</td><td>${h.fechaIngreso}</td><td><span class="badge bg-warning text-dark">${h.estado}</span></td></tr>`).join('')}</tbody></table>
                    </div>
                    <div class="col-md-5">
                        <h2>Estado de Habitaciones</h2>
                        <ul class="list-group">${habitaciones.map(h => `<li class="list-group-item d-flex justify-content-between align-items-center">${h.numero} (${h.tipo})<span class="badge bg-${h.estado === 'Disponible' ? 'success' : 'danger'}">${h.estado}</span></li>`).join('')}</ul>
                    </div>
                </div>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    // --- MÓDULO FACTURAS ---
    async function loadFacturas() {
        try {
            const facturas = await fetchData('/facturas');
            mainContent.innerHTML = `
                <h2>Gestión de Facturas</h2>
                <table class="table table-striped table-hover"><thead><tr><th>ID</th><th>Paciente</th><th>Fecha</th><th>Total</th><th>Estado</th></tr></thead>
                <tbody>${facturas.map(f => `<tr><td>${f.idFactura}</td><td>${f.paciente.nombres} ${f.paciente.apellidos}</td><td>${f.fechaEmision}</td><td>S/ ${f.total.toFixed(2)}</td><td><span class="badge bg-${f.estado === 'Pagado' ? 'success' : 'warning text-dark'}">${f.estado}</span></td></tr>`).join('')}</tbody></table>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    // --- MÓDULO USUARIOS ---
    async function loadUsuarios() {
        try {
            const usuarios = await fetchData('/usuarios');
            mainContent.innerHTML = `
                <h2>Gestión de Usuarios</h2>
                <table class="table table-striped table-hover"><thead><tr><th>ID</th><th>Usuario</th><th>Rol</th></tr></thead>
                <tbody>${usuarios.map(u => `<tr><td>${u.idUsuario}</td><td>${u.nombreUsuario}</td><td><span class="badge bg-secondary">${u.rol}</span></td></tr>`).join('')}</tbody></table>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }

    // --- MÓDULO BITÁCORA ---
    async function loadBitacora() {
        try {
            const registros = await fetchData('/bitacora');
            mainContent.innerHTML = `
                <h2>Bitácora del Sistema</h2>
                <table class="table table-striped table-hover"><thead><tr><th>ID</th><th>Usuario</th><th>Acción</th><th>Fecha y Hora</th></tr></thead>
                <tbody>${registros.map(b => `<tr><td>${b.idBitacora}</td><td>${b.usuario ? b.usuario.nombreUsuario : 'Sistema'}</td><td>${b.accion}</td><td>${new Date(b.fechaHora).toLocaleString('es-PE')}</td></tr>`).join('')}</tbody></table>`;
        } catch (error) { mainContent.innerHTML = `<div class="alert alert-danger">${error.message}</div>`; }
    }
});