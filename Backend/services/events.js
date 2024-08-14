const client = require("../models/database")

async function getEvents(data) {

 if(data.user_id)
 {
  const events = await client.query(
   "select * from "+
   "events inner join event_participant"+
   " on event_participant.event_id = events.id "+
   "and event_participant.participant_id="+data.user_id)
  console.log('Events associated with user ' +data.user_id+ ':', events.rows);
  return events.rows
 }
 return {}
}

async function addEvent(data){
 console.log("addEvent")
 console.log(data)
 if(data.event)
 {
  const event = data.event
  const event_insert_query = {
   text: 'INSERT INTO events(name, date, description,completed) VALUES($1, $2, $3, false) RETURNING *',
   values: [event.name, event.date, event.description],
  };
  
  const res = await client.query(event_insert_query)
  console.log('Event added successfully:', res.rows[0]);
  
  // console.log(event_id)
  const user_insert_query = {
   text: 'INSERT INTO event_participant(event_id, participant_id) VALUES($1, $2) RETURNING *',
   values: [res.rows[0].id, data.user_id],
  };

  const res2 = await client.query(user_insert_query)
  console.log('Event_user added successfully:', res2.rows[0]);
  return {success :true, msg: "event added and user added to event"}   
 }
 return {}
}

async function getEventDetails(data)
{
 const event_details_query = {
  text: 'select * from events where id=$1 ',
  values: [data.event_id],
 };
 const res = await client.query(event_details_query)
 return res.rows[0]
}

async function markEventAsCompleted(data)
{
 const event_details_query = {
  text: 'update events set completed=true where id = $1 RETURNING *',
  values: [data.event_id],
 };
 const res = await client.query(event_details_query)
 return res.rows[0]
}

module.exports = {addEvent,getEvents, getEventDetails, markEventAsCompleted}