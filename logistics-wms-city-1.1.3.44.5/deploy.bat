@echo off

rem -- Lipsion
color 1f
:menu
echo   ________________________________________________________________
echo  ^|                                                                ^|
echo  ^|                     Maven  -  控制面板                         ^|
echo  ^|                                                                ^|
echo  ^|  0 - clean package -D...skip=true -Ptest                       ^|
echo  ^|  1 - clean package -D...skip=true -Pmirror                     ^|
echo  ^|  2 - clean package -D...skip=true -Ponline                     ^|
echo  ^|________________________________________________________________^|
:input
set /p input=-^> 请选择打包类型: 

if "%input%"== "0" goto test
if "%input%"== "1" goto mirror
if "%input%"== "2" goto online
goto end

:test
echo  # 编译测试环境包 #
mvn clean package -Dmaven.test.skip=true -Ptest&&pause&&cls&& call compile.bat

:mirror
echo  # 编译镜像环境包 #
mvn clean package -Dmaven.test.skip=true -Pmirror&&pause&&cls&& call compile.bat

:online
echo  # 编译生产环境包 #
mvn clean package -Dmaven.test.skip=true -Ponline&&pause&&cls&& call compile.bat

rem for /d %%d in (*) do (
rem  if exist %%d\POM.xml set dao_dir=%%d
rem )
rem echo 当前目录是:%dao_dir%
rem &&pause 
:end
echo 结束
prompt
popd