const client = require("../models/database")

async function loginQuery(data) {

 if(data.email)
 {
  const Credentials = await client.query("select * from users where email='"+data.email+"'")
  return Credentials.rows
 }
 if(data.mobileno)
 {
  const Credentials = await client.query("select * from users where mobile='"+data.mobileno+"'")
  return Credentials.rows
 }
 return {err : "email/mobile not provided"}
}

async function signupQuery(data){
 console.log(data)
 if(data.email)
 {
  const users = await client.query("select * from users where email='"+data.email+"'")
  if(users.rows.length!=0)
   return {err : "user with given email already exists"}
 }
 else
  return {err:"email not provided"}
 if(data.mobileno)
 {
  const users = await client.query("select * from users where mobile='"+data.mobileno+"'")
  if(users.rows.length!=0)
  return {err : "user with given mobile no. already exists"}
 }
 else
  return {err:"mobile no. not provided"}

 
 console.log(
  "insert into users values (DEFAULT, '"+
  data.fname+"','"+
  data.mname+"','"+
  data.lname+"','"+
  data.mobileno+"','"+
  data.password+"','"+
  data.email+"')"
 )
 
 await client.query(
  "insert into users values (DEFAULT, '"+
  data.fname+"','"+
  data.mname+"','"+
  data.lname+"','"+
  data.mobileno+"','"+
  data.password+"','"+
  data.email+"')"
 )
 return {}
}
module.exports = {loginQuery,signupQuery}