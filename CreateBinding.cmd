cd ./bundles/archetype/./org.openhab.archetype.binding
mvn clean install
cd ../../binding
mvn archetype:generate -B -DarchetypeGroupId=org.openhab.archetype -DarchetypeArtifactId=org.openhab.archetype.binding -DarchetypeVersion=1.4.0-SNAPSHOT -Dauthor=Mattias -Dversion=1.4.0 -DartifactId=org.openhab.binding.smartbus -Dpackage=org.openhab.binding.smartbus -Dbinding-name=SmartBus
