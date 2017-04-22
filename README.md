项目功能：取activeMQ服务器上对应队列的数据，发起POST请求
项目运行所需环境：linux、activeMQ服务器
目录说明：	
config目录：
	log4j.properties为log4j日志配置文件
	params.properties为程序运行时的参数配置文件
lib目录：
	程序运行所依赖的jar文件
src目录：
	java文件
bin目录：
	编译后的Class文件


*********linux下shell脚本********
#!/bin/sh

basepath=$(cd `dirname`$0;pwd)

echo $basepath
cd ..
echo $PWD

pwd=$PWD
for i in lib/*;
	
do 
CLASSPATH=$CLASSPATH:$PWD/$i

done

echo $CLASSPATH

cd bin

java -cp $CLASSPATH work.BeginReceiver >> info.log 2>&1 &
