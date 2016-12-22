__author__ = 'dhcdht'

import os
import re

plugin_path = os.path.expanduser('~/Library/Application Support/Developer/Shared/Xcode/Plug-ins')
# Xcode 8.2.1
plugin_uuid = 'E0A62D1F-3C18-4D74-BFE5-A4167D643966'

for root, dirs, files in os.walk(plugin_path):
    for file in files:
        if file == 'Info.plist' and root.endswith('xcplugin/Contents'):
            plist_path = os.path.join(root, file)
            print(plist_path)
            fp = open(plist_path, 'r')
            plist_content = fp.read()
            fp.close()
            if re.search('DVTPlugInCompatibilityUUIDs', plist_content):
                if not re.search(plugin_uuid, plist_content):
                    fp = open(plist_path, 'w')

                    plist_replaced = re.sub('<key>DVTPlugInCompatibilityUUIDs</key>\s*<array>',
                                            '<key>DVTPlugInCompatibilityUUIDs</key>\n'
                                            '	<array>\n'
                                            '		<string>%s</string>' % plugin_uuid,
                                           plist_content)
                    fp.write(plist_replaced)
                    fp.close()
                    print('add : ' + plist_path)
                else:
                    print('Dont need add : ' + plist_path)
