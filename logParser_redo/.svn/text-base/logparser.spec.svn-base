Summary: LogParser RPM
Name: HITV_LogParser
Version: 5.3.1
Release: 2
Source: logparser.tar
Buildroot: %{_tmppath}/%{name}-%{version}-%{release}-root
License: GPL
Packager: Terry
Group: BI
URL: http://hitv.hisense.com
Vendor: HISENSE MEDIA NETWORKS Co.Ltd
Distribution: SUSE Linux Enterprise 11 SP1 & SP2
%description 
Provide log parser service.
Author:
-------
        Terry <tianyuqi@hisense.com>
%prep
%setup -c
%pre
	mountnum=`mount|grep /usr/logparser/Log|wc -l`
	if [ $mountnum -gt 0 ]
	then
	  echo 'Please umount Log directory first!!!'
	  exit 1
	fi
	num=`ps -ef|grep logparser.jar|grep -v grep|wc -l`
	if [ $num -gt 0 ]
	then
	  kill -9 `ps -ef|grep logparser.jar|grep -v grep|awk '{print $2}'`
	fi
	rm -rf /usr/logparser/lib
	if [ ! -d /usr/backupconfig/logparser ]; then
	  mkdir -p /usr/backupconfig/logparser
	fi
	if [ -f /usr/logparser/config/sys_conf.properties ]; then
	  cp /usr/logparser/config/sys_conf.properties /usr/backupconfig/logparser/sys_conf.properties.bak
	fi
%post
	if [ -f /usr/backupconfig/logparser/sys_conf.properties.bak ]; then
	  cp /usr/backupconfig/logparser/sys_conf.properties.bak /usr/logparser/config/sys_conf.properties
	  rm /usr/backupconfig/logparser/sys_conf.properties.bak
	fi
%preun
	mountnum=`mount|grep /usr/logparser/Log|wc -l`
	if [ $mountnum -gt 0 ]
	then
	  echo 'Please umount Log directory first!!!'
	  exit 1
	fi
	num=`ps -ef|grep logparser.jar|grep -v grep|wc -l`
	if [ $num -gt 0 ]
	then
	  kill -9 `ps -ef|grep logparser.jar|grep -v grep|awk '{print $2}'`
	fi
	if [ ! -d /usr/backupconfig/logparser ]; then
	  mkdir -p /usr/backupconfig/logparser
	fi
	if [ -f /usr/logparser/config/sys_conf.properties ]; then
	  cp /usr/logparser/config/sys_conf.properties /usr/backupconfig/logparser/sys_conf.properties.bak
	fi
%install
rm -rf ${RPM_BUILD_ROOT}/usr/*
mkdir -p ${RPM_BUILD_ROOT}/usr/
cp -ar ${RPM_BUILD_DIR}/%{name}-%{version}/logparser ${RPM_BUILD_ROOT}/usr/logparser
%files
/usr/logparser
%defattr(755,root,root)
%clean
rm -rf ${RPM_BUILD_ROOT}
rm -rf ${RPM_BUILD_DIR}/*