import http from "http";
import express from "express";
import routes from "./routes";

const router = express();
const { PORT = 3000 } = process.env;
const server = http.createServer(router);

router.use("/", routes);

server.listen(PORT, () =>
  console.log(`Server is running http://localhost:${PORT}...`)
);
