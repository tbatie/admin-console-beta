const schema = require('./target/schema.json')

module.exports = require('babel-relay-plugin')(schema, {
  debug: true,
  suppressWarnings: false,
  enforceSchema: true
})
