import http from "http";
import express from "express";
import routes from "./routes";
import bodyParser from "body-parser";

// Importa bibliotecas para criação do web server
const router = express();
const { PORT = 3000 } = process.env;
const server = http.createServer(router);

// Importa bibliotecas no web server
router.use(bodyParser.urlencoded({ extended: false }))
router.use(bodyParser.json())

// Importa configuração de rotas do módulo "routes"
router.use("/", routes);

// Inicia execução do servidor
server.listen(PORT, () =>
  console.log(`Server is running http://localhost:${PORT}...`)
);
