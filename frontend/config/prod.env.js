'use strict'
let HOST = process.argv.splice(2)[0] || 'prod';//使用 npm run build -- xxx，传递进来的参数xxx
module.exports = {
  NODE_ENV: '"production"',
  HOST: '"'+HOST+'"'
}
