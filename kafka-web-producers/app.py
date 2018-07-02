from flask import Flask, render_template
from confluent_kafka import Producer

app = Flask(__name__)

@app.route("/")
def show_form():
	return render_template('submit-form.html')

if __name__=='__main__':
	app.run(host='0.0.0.0', port=80)