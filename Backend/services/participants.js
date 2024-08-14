const client = require("../models/database")

async function addParicipant(data) {
 if(data.event_id&&data.user_id)
 {
  const participant_insert_query = {
   text: 'insert into event_participant VALUES($1, $2)',
   values: [data.event_id, data.user_id],
  };
  console.log(participant_insert_query)
  await client.query(participant_insert_query)
  return {success: true}
 }
 return {success:false}
}

async function getParticipants(data){
 const get_parties_query = {
  text: "select participant_id, fname, lname  from"+
  " event_participant inner join users"+
  " on participant_id = users.id "+
  "where event_id=$1",
  values: [data.event_id],
 };

 const res = await client.query(get_parties_query)
 return res.rows  
}

async function removeParticipant(data){

}
module.exports = {addParicipant, getParticipants, removeParticipant}