import http from "http";
import express from "express";
import routes from "./routes";
import bodyParser from "body-parser";

const router = express();
const { PORT = 3000 } = process.env;
const server = http.createServer(router);

router.use(bodyParser.urlencoded({ extended: false }))
router.use(bodyParser.json())
router.use("/", routes);

server.listen(PORT, () =>
  console.log(`Server is running http://localhost:${PORT}...`)
);
