const schema = require('./target/schema.json')

module.exports = require('babel-relay-plugin')(schema.data, {
  debug: true,
  suppressWarnings: false,
  enforceSchema: true
})
