const client = require("../models/database")

async function addItem(data) {
 if(data.user_id)
 {
  const item_insert_query = {
   text: 'insert into items VALUES(default, $1, $2, $3) RETURNING *',
   values: [data.name, data.description, data.expense],
  };
  const res = await client.query(item_insert_query)
  console.log('Event added successfully:', res.rows[0]);

  const event_item_query = {
    text: 'insert into event_items VALUES($1, $2) RETURNING *',
    values: [data.event_id, res.rows[0].id],
  };
  const res2 = await client.query(event_item_query);
  console.log('Event item created succesfully', res2.rows[0]);

  const item_payment_query = {
      text: 'insert into item_payings VALUES($1, $2, $3) RETURNING *',
      values: [res.rows[0].id, data.user_id, data.expense],
    };
  const res3 = await client.query(item_payment_query)
  console.log('Initial item payment succesfully registered', res3.rows[0]);

  const item_owings_query = {
    text: 'insert into item_owings VALUES($1, $2, $3) RETURNING *',
    values: [res.rows[0].id, data.user_id, data.expense],
  };
  const res4 = await client.query(item_owings_query)
  console.log('Initial item owing`s succesfully registered', res4.rows[0]);

  return {success: true, item_id: res.rows[0].id}
 }
return {success:false}
}

async function getItems(data){
 const get_items_query = {
  text: "select * from"+
  " items, event_items "+
  "where items.id = event_items.item_id "+
  "and event_items.event_id=$1",
  values: [data.event_id],
 };
 console.log(get_items_query)
 const res = await client.query(get_items_query)
 return res.rows
}

async function deleteItem(data){
    // const delete_owings_query = {
    //     text: "delete from item_owings where item_id = $1",
    //     values: [data.item_id],
    // };
    // console.log(delete_owings_query)
    // await client.query(delete_owings_query)
    // console.log('Required Item owings succesfully deleted');
    // const delete_payings_query = {
    //     text: "delete from item_payings where item_id = $1",
    //     values: [data.item_id],
    // };
    // console.log(delete_payings_query)
    // await client.query(delete_payings_query)
    // console.log('Required Item payings succesfully deleted');
    // const delete_event_items_query = {
    //     text: "delete from event_items where item_id = $1",
    //     values: [data.item_id],
    // };
    // console.log(delete_event_items_query)
    // await client.query(delete_event_items_query)
    // console.log('Required Item events succesfully deleted');

 const delete_items_query = {
  text: "delete from items where item_id = $1",
  values: [data.item_id],
 };
 await client.query(delete_items_query)
 console.log('Required Items succesfully deleted');
 return {success: true}
}

async function updateItem(data){
 console.log(data)
 //items
 const item_name_update_query = {
  text: 'update items set name = $1 where id = $2 RETURNING *',
  values: [data.name,data.item_id],
 };
 const res = await client.query(item_name_update_query)
 const item_desc_update_query = {
  text: 'update items set description = $1 where id = $2 RETURNING *',
  values: [data.description,data.item_id],
 };
 const res2 = await client.query(item_desc_update_query)
 const item_expense_update_query = {
  text: 'update items set expense = $1 where id = $2 RETURNING *',
  values: [data.expense,data.item_id],
 };
 const res3 = await client.query(item_expense_update_query)
    
 //item_payings
 const delete_payings_query = {
  text: "delete from item_payings where item_id = $1",
  values: [data.item_id],
 };
 await client.query(delete_payings_query)

 for(let i=0;i<data.payers.length;i++){
  const item_payings_query = {
   text: 'insert into item_payings VALUES($1, $2, $3)',
   values: [data.item_id, data.payers[i].user_id, data.payers[i].amount],
  };
  console.log(item_payings_query)
  await client.query(item_payings_query)
 }

 //item_owings
 const delete_owings_query = {
  text: "delete from item_owings where item_id = $1",
  values: [data.item_id],
 };
 await client.query(delete_owings_query)
 for(let i=0;i<data.owers.length;i++){
  const item_owings_query = {
   text: 'insert into item_owings VALUES($1, $2, $3)',
   values: [data.item_id, data.owers[i].user_id, data.owers[i].amount],
  };
  console.log(item_owings_query)
  await client.query(item_owings_query)
 }
 
 //payments
 const delete_payments_query = {
  text: "delete from payments where item_id = $1",
  values: [data.item_id],
 };
 await client.query(delete_payments_query)
 for(let i=0;i<data.payments.length;i++){
  const payments_query = {
   text: 'insert into payments VALUES($1, $2, $3, $4)',
   values: [data.payments[i].payer_id, data.payments[i].reciever_id, data.item_id, data.payments[i].amount],
  };
  console.log(payments_query)
  await client.query(payments_query)
 }
}


async function getItem(data)
{
 const get_items_query = {
  text: "select * from"+
  " items where id=$1",
  values: [data.item_id],
 };
 const item = await client.query(get_items_query)

 const get_payers = {
  text: "select * from item_payings where item_id = $1",
  values: [data.item_id],
 };
 const payers = await client.query(get_payers)
 
 const get_owers = {
  text: "select * from item_owings where item_id = $1",
  values: [data.item_id],
 };
 const owers = await client.query(get_owers)

 // const get_parties = {
 //  text: "select * from  where item_id = $1",
 //  values: [data.item_id],
 // };
 // const parties = await client.query(get_parties)

 
 return {item: item.rows[0], payings: payers.rows, owings: owers.rows}
}

module.exports = {addItem, getItems, deleteItem, updateItem, getItem}