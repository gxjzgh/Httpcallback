��Ŀ���ܣ�ȡactiveMQ�������϶�Ӧ���е����ݣ�����POST����
��Ŀ�������軷����linux��activeMQ������
Ŀ¼˵����	
configĿ¼��
	log4j.propertiesΪlog4j��־�����ļ�
	params.propertiesΪ��������ʱ�Ĳ��������ļ�
libĿ¼��
	����������������jar�ļ�
srcĿ¼��
	java�ļ�
binĿ¼��
	������Class�ļ�


*********linux��shell�ű�********
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
