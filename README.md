| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registra un usuario |
| POST | `/api/auth/login` | Inicia sesión |
| GET | `/api/users/profile?email=ana12345@test.com` | Consulta el perfil de un usuario |
| PUT | `/api/users/{id}` | Actualiza nombre, correo y opcionalmente contraseña o rol |
| POST | `/api/books` | Registra un libro |
| PUT | `/api/books/{id}` | Actualiza la información de un libro |
| GET | `/api/books?category=Fantasia&minPrice=10&maxPrice=50` | Lista libros y filtra por género o precio |
| GET | `/api/books/search?q=harryh` | Busca libros por título, autor o género |
| GET | `/api/books/{id}` | Consulta el detalle de un libro |
| POST | `/api/clients` | Registra un cliente |
| GET | `/api/clients` | Lista los clientes |
| GET | `/api/clients/{id}` | Consulta un cliente |
| PUT | `/api/clients/{id}` | Actualiza los datos de un cliente |
| POST | `/api/sales` | Registra una venta |
| GET | `/api/sales?idClient=1&idUser=1` | Lista ventas (filtros opcionales) |
| GET | `/api/sales/{id}` | Consulta una venta |
| POST | `/api/sale-details` | Agrega un detalle (libro y cantidad) a una venta |
| GET | `/api/sale-details?idSale=15` | Lista detalles de venta (filtro opcional) |
| GET | `/api/sale-details/{id}` | Consulta un detalle de venta |
