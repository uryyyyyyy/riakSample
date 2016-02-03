
setting

docker build
docker run -i

# in docker container

vim /etc/riak/riak.conf
# set 127.0.0.1 -> your local IP
riak start

# set at least 3 machines
# create cluster
riak-admin cluster join riak@172.17.0.2

# check cluster member
riak-admin member-status










ManyLoad.java

set 10000 times and check the time

server: riak on docker on localhost
client: localhost java

```
cluster start done
21628 ms
my_value333
```
