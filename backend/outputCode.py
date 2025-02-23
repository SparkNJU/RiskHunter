import os

def scan_project_files(project_path, output_file):
    """
    扫描指定项目路径下的 .java, .sql, 和 pom.xml 文件，并将文件名、路径和内容写入到输出文件中，
    同时统计每个文件的行数以及所有代码的总行数。在文件名、路径、行数和内容之间增加换行符。
    """
    total_line_count = 0  # 初始化总行数
    file_count = 0 # 初始化文件数

    with open(output_file, 'w', encoding='utf-8') as f:
        for root, dirs, files in os.walk(project_path):
            for file in files:
                file_path = os.path.join(root, file)
                
                if file.endswith('.java') or file.endswith('.sql') or file == 'pom.xml':
                    file_count += 1
                    try:
                        # 读取文件内容并统计行数
                        with open(file_path, 'r', encoding='utf-8') as source_file:
                            content = source_file.read()
                            lines = content.splitlines()
                            line_count = len(lines)
                            total_line_count += line_count  # 累加到总行数

                        # 写入分隔线
                        f.write('\
' + '='*80 + '\
')
                        # 写入文件名
                        f.write(f'File Name: {file}\
\
')  # 增加换行
                        # 写入文件路径
                        f.write(f'File Path: {file_path}\
\
')  # 增加换行
                        # 写入文件行数
                        f.write(f'Line Count: {line_count}\
\
')  # 增加换行
                        f.write('\
File Content:\
\
')  # 增加换行
                        
                        # 写入文件内容
                        f.write(content)
                            
                    except Exception as e:
                        f.write(f'\
Error reading file: {str(e)}\
')

        # 扫描结束后，写入总行数和文件总数
        f.write('\
' + '='*80 + '\
')
        f.write(f'Total Line Count: {total_line_count}\
\
')  # 增加换行
        f.write(f'Total File Count: {file_count}\
\
')  # 增加换行


if __name__ == '__main__':
    project_path = os.getcwd()
    output_file = 'projectCode.txt'
    
    if os.path.exists(project_path):
        scan_project_files(project_path, output_file)
        print(f"扫描完成! 结果已保存到 {output_file}")
    else:
        print("项目路径不存在!")