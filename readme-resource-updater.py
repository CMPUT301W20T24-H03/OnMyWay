# import os
# import re
from glob import glob
from os import path
from re import sub, findall



# Entry point
def main():
	# Load a single file
	def Load(filename):
		if path.exists(filename):
			try:
				file = open(filename, 'r')
				text = file.read()
				file.close()

				return text

			except OSError:
				raise Exception(filename + ' could not be read')

		else:
			raise Exception(filename + ' does not exist')



	# Save a single file
	def Save(filename, data):
		try:
			file = open(filename, 'w')
			file.write(data)
			file.close()
			print('Updated ' + filename)

		except OSError:
			raise Exception(filename + ' could not be written')



	# Format URLs in Markdown
	def FormatUrls(code):
		return sub(r'(https?:\/\/(?:www\.)?[-a-zA-Z0-9@:%._\+~#=]{1,256}\.[a-zA-Z0-9()]{1,6}\b[-a-zA-Z0-9()@:%_\+.~#?&\/\/=]*)', r'[Link](\1)',	code)



	# Remove whitespace from a comment
	def RemoveWhitespace(code):
		return sub(r'\n[\t ]*', r'\\\n', code)



	# Remove garbage character from a comment
	# def StripUselessCharacters(code, regex_1):
	# 	return sub(regex_1, '',	# Remove last backslash, comment escape chars, and whitespace
	# 		sub(r'\n[\t ]*', r'\\\n', code)	# Remove whitespace
	# 	)


	# Extract resources from code
	# def ParseResources(code, regex_1, regex_2):
	# 	return list(
	# 		map(
	# 			lambda resource: '- ' + FormatUrls(StripUselessCharacters(code, regex_1)), findall(regex_2, code)	# Find comments
	# 		)
	# 	)



	# Extract resources from code
	def ParseJavaResources(code):
		# return ParseResources(code, r'(?:\/{3}.*\n[\t ]*){2,3}', r'(?:[\t ]*\/{3}[\t ]*|\\$)')
		return list(
			map(
				lambda resource: '- ' +
					FormatUrls(
						sub(r'(?:[\t ]*\/{3}[\t ]*|\\$)', '',	# Remove last backslash, comment escape chars, and whitespace
							RemoveWhitespace(resource)
						)
					),
					findall(r'(?:\/{3}.*\n[\t ]*){1,3}', code)	# Find comments
			)
		)



	# Extract resources from code
	def ParseResResources(code):
		# return ParseResources(code, r'(?:<!---.*\n[\t ]*){2,3}', r'(?:[\t ]*<!---[\t ]*|[\t ]*--->|\\$)')
		return list(
			map(
				lambda resource: '- ' +
					FormatUrls(
						sub(r'(?:[\t ]*<!---[\t ]*|[\t ]*--->|\\$)', '',	# Remove last backslash, comment escape chars, and whitespace
							RemoveWhitespace(resource)
						)
					),
					findall(r'(?:<!---.*\n[\t ]*){1,3}', code)	# Find comments
			)
		)



	# Join list items into a string
	def Concatenate(resource_list):
		return '\n'.join(resource_list)



	# Add resources to readme file
	def ReplaceReadmeText(readme, resources_string):
		return sub(r'## Resources\n((?:.*\n)*)#', '## Resources\n' + resources_string + '\n\n#', readme)



	# Get comments from all Java files
	def CollectJavaCode(package_directory):
		code_list = []

		for file_name in glob('app/src/main/java/' + package_directory + '/*.java'):
			code_list.append(Load(file_name))

		return Concatenate(code_list)



	# Get comments from all Java files
	def CollectResCode(package_directory):
		code_list = []
		path_start = 'app/src/main/res/'
		path_end = '/*.xml'
		file_name_list = glob(path_start + 'drawable'  + path_end)
		file_name_list.extend(glob(path_start + 'drawable-24'  + path_end))
		file_name_list.extend(glob(path_start + 'layout'  + path_end))
		file_name_list.extend(glob(path_start + 'mipmap-anydpi-v26'  + path_end))
		file_name_list.extend(glob(path_start + 'values'  + path_end))

		for file_name in file_name_list:
			code_list.append(Load(file_name))

		return Concatenate(code_list)



	# Join all resources into one string
	def JoinResources(package_directory):
		resource_list = ParseJavaResources(CollectJavaCode(package_directory))
		resource_list.extend(ParseResResources(CollectResCode(package_directory)))

		return Concatenate(resource_list)



	readme_name = 'README.md'
	package_directory = 'com/CMPUT301W20T24/OnMyWay'

	# print(JoinResources(package_directory))

	Save(readme_name, ReplaceReadmeText(Load(readme_name), JoinResources(package_directory)))



main()
