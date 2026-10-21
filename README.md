# Rincón Sur · Control de flujo de personas

Aplicación Android para una tienda departamental que registra el flujo de personas por acceso y distingue
entre quienes **transitan** y quienes **compran**. Funciona 100 % en local, sin servidor.

Evaluación Parcial 2 · Desarrollo de Aplicaciones Móviles · DuocUC 


| Integrantes: Javiera Rebolledo - Javier Lara |
| Docente: Sergio Fuentes |
| Repositorio en GitHub: https://github.com/JavierIgnacioLara/Rincon-Sur |


Tecnologías
- Kotlin, Jetpack Compose y Material 3
- Arquitectura MVVM (Pantalla → ViewModel → Repositorio → DAO → Room)
- Room 2.8.1 con KSP (SQLite local)
- DataStore Preferences (opción «Recordar sesión»)
- Recursos Nativos (cámara, vibración)
- Navigation Compose
- Corrutinas y `StateFlow`
- `minSdk 33` · `targetSdk 37`

## Cómo ejecutarla
1. Clona el repositorio y ábrelo con **Android Studio**.
2. Espera a que termine **Sync** de Gradle.
3. Elige un emulador (por ejemplo, Pixel 7 Pro) o un teléfono con depuración USB.
4. Presiona **Run ▶**.

La primera vez, crea una cuenta desde «Crear cuenta». Si cambias el esquema de la base de datos,
sube `version` en `AppDatabase` y **desinstala la app** del emulador antes de volver a ejecutar.

## Qué hace la app
1. **Inicio de sesión** con correo y contraseña (con ojo para verla) y casilla «Recordar sesión».
2. **Crear cuenta:** nombre, correo, contraseña y confirmación, con validación por campo.
3. **Inicio:** saludo con el nombre del usuario, fecha y hora, foto opcional con la cámara,
   «Empezar turno» y «Cerrar turno».
4. **Registrar persona:** acceso de entrada, género, rango de edad, tipo de registro (Transitó o Compró),
   acceso de salida y **acompañantes** (adultos y niños) con contadores. Guarda en SQLite y suma al contador del turno.
5. **Accesos:** una tarjeta por acceso con sus totales y el total del turno activo. Cada tarjeta abre su **Detalle**.
6. **Historial:** selector de fecha, pestañas Total y Acceso 1–4, resumen del día y lista de registros.
7. Barra inferior con **Inicio, Accesos e Historial**. En horizontal pasa a un riel lateral.

## Recursos nativos
- **Cámara:** permiso `CAMERA` solicitado en ejecución, explicación si se rechaza, y la app sigue funcionando sin ella.
  La foto se guarda con `FileProvider`.
- **Vibración:** vibración corta (`HapticFeedback`) al guardar un registro.

## Decisiones de diseño
- Accesos y Detalle muestran el **turno activo**. Historial muestra todos los registros de la **fecha elegida**.
- Los totales se calculan con los registros guardados, no hay números fijos.
- No se puede registrar una persona sin un turno activo.
- Validaciones como funciones puras que devuelven `String?`, en `validation/Validators.kt`.
- Manejo de errores por campo: cada pantalla tiene su `UIState` con un objeto de errores.
- El estado del formulario vive en el ViewModel y sobrevive a la rotación de pantalla.
- Prototipo: la contraseña se guarda tal cual en la base local, sin cifrado.

## Estructura del proyecto
```
com.example.proyectorinconsur/
├── data/          Room (AppDatabase, DAO, entidades), repositorios, sesión y DataStore
├── model/         clases del dominio y estados de pantalla con sus errores
├── navigation/    rutas y grafo de navegación
├── ui/            pantallas, componentes, layouts y tema
├── usecase/       cálculo de estadísticas
├── utils/         fechas, recursos nativos y orientación
├── validation/    validaciones de formularios
├── viewmodel/     un ViewModel por pantalla
└── MainActivity.kt
```

## Flujo de datos
```
Pantalla (Compose) → ViewModel (StateFlow) → Repositorio → DAO → Room (SQLite)
        ↑                                                           │
        └───────────────────────── Flow ────────────────────────────┘
```

## Limitaciones conocidas
- La fecha y la hora salen del reloj del dispositivo.
- El rol (administrador u operador) se guarda, pero todavía no cambia permisos.
- La contraseña no está cifrada y no hay bloqueo por intentos fallidos, por el alcance de prototipo.
- Si Android cierra la app por falta de memoria, un formulario a medias puede perderse.
- La aplicacion cumple con los requerimientos de Ricon-Sur Guarda resgitro de personas se maneja el historial y se puede filtrar por fechas y accesos.
