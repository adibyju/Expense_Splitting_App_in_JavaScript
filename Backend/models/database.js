var pg = require("pg")
var fs = require("fs")
const {Client} = pg;
pg.types.setTypeParser(1114, str => str);

const client = new Client(
  JSON.parse(fs.readFileSync('./config.txt', 'utf8'))
)

client.connect((err) => {
  if (err) {
    console.error('connection error', err.stack)
  } else {
    console.log('connected')
  }
})


module.exports = client