| Método | Ruta | Descripción |
|--------|------|-------------|
| POST | `/api/auth/register` | Registra un usuario |
| POST | `/api/auth/login` | Inicia sesión |
| GET | `/api/users/profile?email=ana12345@test.com` | Consulta el perfil de un usuario |
| GET | `/api/books?category=Fantasia&minPrice=10&maxPrice=50` | Lista libros y filtra por género o precio |
| GET | `/api/books/search?q=harryh` | Busca libros por título, autor o género |
| GET | `/api/books/{id}` | Consulta el detalle de un libro |
| POST | `/api/cart/items` | Agrega un libro al carrito |
| GET | `/api/cart?email=ana12345@test.com` | Consulta el carrito de un usuario |
| POST | `/api/purchases` | Compra los libros del carrito |
| GET | `/api/purchases?email=ana12345@test.com` | Consulta el historial de compras |
