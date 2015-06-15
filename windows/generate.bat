mkdir WindowsPhone.ARM
mkdir WindowsPhone.Win32
mkdir WindowsStore.Win32

cd WindowsPhone.ARM
cmake -G "Visual Studio 12 2013 ARM" -DCMAKE_SYSTEM_NAME=WindowsPhone -DCMAKE_SYSTEM_VERSION=8.1 ..

cd ../WindowsPhone.Win32
cmake -G "Visual Studio 12 2013" -DCMAKE_SYSTEM_NAME=WindowsPhone -DCMAKE_SYSTEM_VERSION=8.1 ..

cd ../WindowsStore.Win32
cmake -G "Visual Studio 12 2013" -DCMAKE_SYSTEM_NAME=WindowsStore -DCMAKE_SYSTEM_VERSION=8.1 ..

cd ..