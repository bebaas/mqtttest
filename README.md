# mqtttest
demo of mqtt with spring boot

# test sub
1. run this project, it will sub title "test01" and "test02" by default
2. run mosquitto_pub in your own pc:mosquitto_pub -h 173.82.255.254 -p 1883 -u test2 -P test2 -t test01 -m '{"a":1, "b":2}'
3. check the output of the log, it will get the message -> {"a":1, "b":2}

# test pub
1. run mosquitto_sub in your own pc:mosquitto_sub -h 173.82.255.254 -p 1883 -u test2 -P test2 -d -v -t test
2. run this project
3. access this url in your browser: http://localhost:8801/pubtest
4. check the output of mosquitto_sub
