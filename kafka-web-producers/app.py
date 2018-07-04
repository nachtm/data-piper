from flask import Flask, render_template, request
import json
import sys
from confluent_kafka import Producer

#this is an ugly fix for the scaling port-assignment problem. as soon as we want to scale this up, 
#this will need to be fixed.
BROKER_ADDR = 'kafka-brokers:9092'

app = Flask(__name__)

#Right now, we have very low throughput so flush after every message to ensure each message gets sent.
#In some scenario (where kafka would actually be useful) where we want to batch our requests up,
#delete p.flush().
@app.route("/", methods=["GET", "POST"])
def show_form():
	if request.method == "GET":
		with open('schema.json') as f:
			features = json.loads(''.join(f.readlines()))
			return render_template('submit-form.html', schema=features)
	else:
		p = Producer({'bootstrap.servers': BROKER_ADDR})
		p.produce('mushrooms', value=str(request.form))
		p.flush()
		return str(request.form)

if __name__=='__main__':
	app.run(host='0.0.0.0', port=80)