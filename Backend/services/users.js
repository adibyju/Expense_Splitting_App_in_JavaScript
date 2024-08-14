const client = require("../models/database")

async function getId(data) {

 if(data.email)
 {
  const user = await client.query(
   "select * from "+
   "users where email = '"+data.email+"'")
  if(user.rows.length>0)
  return user.rows[0].id
 }
 else if(data.mobileno)
 {
  const user = await client.query(
   "select * from "+
   "users where mobile = '"+data.mobileno+"'")
  if(user.rows.length>0)
   return user.rows[0].id 
 }
}


module.exports = {getId}