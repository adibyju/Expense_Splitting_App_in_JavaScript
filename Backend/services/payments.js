const client = require("../models/database")

async function addPayment(data) {

 const event = data.event
 const payment_insert_query = {
  text: 'INSERT INTO payments(payer_id, receiver_id, amount) VALUES($1, $2, $3) RETURNING *',
  values: [data.payer_id, data.receiver_id, data.amount],
 };
 

 const res = await client.query(payment_insert_query)
 // console.log('Event added successfully:', res.rows[0]);
}

async function getPayments(data)
{
 // const payer_query = {
 //  text: 'select reciever_id, SUM(amount) from payments where payer_id = $1 group by reciever_id',
 //  values: [data.user_id],
 // };
 // const payer = await client.query(payer_query)
 
 // const receiver_query = {
 //  text: 'select payer_id, SUM(amount) from payments where reciever_id = $1 group by payer_id',
 //  values: [data.user_id],
 // };
 
 const balance_query = {
  text: "with bal as "+
  "(select other_id, sum(p) from "+
  "((select receiver_id as other_id, SUM(amount) as p from payments where payer_id = $1 group by receiver_id) "+
  "union "+
  "(select payer_id as other_id, -SUM(amount) as p from payments where receiver_id = $1 group by payer_id)) as x "+
  "group by x.other_id) "+
  "select other_id, fname, lname, sum  from bal join users on other_id = id",
  values: [data.user_id]
 }
 console.log(balance_query)
 const balance = await client.query(balance_query)

 return balance.rows
}




module.exports = {addPayment, getPayments}