# echo `LGType_NGB40 = 4 LGType_NGB30 = 5 LGType_ADM = 2 LGType_STV = 3
num=`ps -ef|grep logparser.jar|grep -v grep|wc -l`
if [ $num -gt 0 ]
then
    echo "already run!"
    exit 1
else
    /usr/src/jdk1.6.0_10/bin/java -Xms300m -Xmx400m -jar /usr/logparser/logparser.jar 3 &
fi