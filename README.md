# restful_path_lister
RESTful path lister

______________
INSTALLATION:
______________

$<INSTALL_DIR>/restful_path_lister $ docker build -t restful_path_lister build/

_______
TO RUN:
_______

1) Start the service:

$<INSTALL_DIR>/restful_path_lister $ docker run -d -it -p 8080:8080 --volume=<LOCAL_VOLUME>:/mnt/localfs/  restful_path_lister

NOTE: make sure to set the <LOCAL_VOLUME> field to your local system folder of preference.
E.g
$<INSTALL_DIR>/restful_path_lister $ docker run -d -it -p 8080:8080 --volume=/home/:/mnt/localfs/  restful_path_lister

2) Enter test url:
http://localhost:8080/listdir?path=/usr/



