const express = require('express')
const cors = require('cors');
var morgan = require('morgan')

const app = express();
const port = 8081;

app.use(cors());
app.use(morgan('tiny'))


app.get('/api/sre/v1/extra/ok', (req, res) => {
    res.status(200).send("ok");
});

app.get('/api/sre/v1/extra/bulkhead', async (req, res) => {
    await new Promise(r => setTimeout(r, 20000));
    res.status(200).send("ok");
});

app.get('/api/sre/v1/extra/nok404', (req, res) => {
    res.status(404).send("Erro 404 - Not Found");
});

app.get('/api/sre/v1/extra/nok423', (req, res) => {
    res.status(423).send("Erro 423 - Locked");
});

app.get('/api/sre/v1/extra/nok425', (req, res) => {
    res.status(425).send("Erro 425 - Too Early");
});

app.get('/api/sre/v1/extra/nok500', (req, res) => {
    res.status(500).send("Erro 500 - Internal Server Error");
});

app.get('/api/sre/v1/extra/nok504', (req, res) => {
    res.status(504).send("Erro 504 - Gateway Timeout");
});

app.listen(port, () => console.log(`Demo SRE Extras working on port ${port}!`));
