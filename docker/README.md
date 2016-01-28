
## build image

sudo docker build -t uryyyyyyy/mynginx .

## run container

sudo docker run -d -p 8000:80 --name mynginx uryyyyyyy/mynginx

## remove container

sudo docker rm mynginx

## remove image

sudo docker rmi uryyyyyyy/mynginx
