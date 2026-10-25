| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registra un usuario |
| POST | `/api/auth/login` | Inicia sesión |
| GET | `/api/users` | Lista los usuarios |
| GET | `/api/users/{email}` | Consulta el perfil de un usuario |
| PUT | `/api/users/{id}` | Actualiza nombre, correo y opcionalmente contraseña o rol |
| POST | `/api/books` | Registra un libro |
| PUT | `/api/books/{id}` | Actualiza la información de un libro |
| GET | `/api/books?category=Fantasia&minPrice=10&maxPrice=50` | Lista libros y filtra por género o precio |
| GET | `/api/books` | Consulta todos los libros |
| POST | `/api/clients` | Registra un cliente |
| GET | `/api/clients` | Lista los clientes |
| PUT | `/api/clients/{id}` | Actualiza los datos de un cliente |
| GET | `/api/sales` | Consulta todas las ventas |
| POST | `/api/sales` | Registra una venta |
| GET | `/api/sales/{id}` | Consulta una venta |
| POST | `/api/sale-details` | Agrega un detalle (libro y cantidad) a una venta |
| GET | `/api/sale-details` | Consulta los detalles de venta |
| GET | `/api/sale-details/{id}` | Consulta un detalle de venta |
