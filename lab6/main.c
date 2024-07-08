#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct File {//�ļ�
    char name[32];
    char *content;
    int size;

} File;

typedef struct Directory {//Ŀ¼
    char name[32];
    File *files[32];
    int file_count;//Ŀ¼����
    struct Directory *subdirectories[32];//��Ŀ¼����
    int subdir_count;//��Ŀ¼����
    struct Directory *parent_directory;//��Ŀ¼
} Directory;

Directory *root;// �ļ�ϵͳ��Ŀ¼
Directory *current;// ��ǰĿ¼

Directory *create_directory(const char *name) {//�½�Ŀ¼
    Directory *dir = (Directory *) malloc(sizeof(Directory));//�����ڴ�
    strcpy(dir->name, name);
    dir->file_count = 0;
    dir->subdir_count = 0;
    dir->parent_directory = NULL;
    return dir;
}

File *create_file(const char *name, const char *content) {//�½��ļ�
    File *file = (File *) malloc(sizeof(File));//�����ڴ�
    strcpy(file->name, name);
    file->content = strdup(content);
    file->size = strlen(content);
    return file;
}

void add_file_to_directory(Directory *dir, File *file) {//��Ŀ¼������ļ�
    dir->files[dir->file_count++] = file;
}

void add_subdirectory(Directory *parent, Directory *child) {//��Ŀ¼���½��ļ��У�Ŀ¼��
    parent->subdirectories[parent->subdir_count++] = child;
    child->parent_directory = parent;
}

void list_directory(Directory *dir) {//Ŀ¼�б�-ls�鿴
    printf("Directory: %s\n", dir->name);
    int i;
    for (i = 0; i < dir->file_count; i++) {
        printf(" File: %s (%d bytes)\n", dir->files[i]->name, dir->files[i]->size);
    }
    for ( i = 0; i < dir->subdir_count; i++) {
        printf(" Subdirectory: %s\n", dir->subdirectories[i]->name);
    }
}

Directory *find_subdirectory(Directory *dir, const char *name) {//������Ŀ¼
    int i;
    for ( i = 0; i < dir->subdir_count; i++) {
        if (strcmp(dir->subdirectories[i]->name, name) == 0) {
            return dir->subdirectories[i];
        }
    }
    return NULL;
}

File *find_file(Directory *dir, const char *name) {//�����ļ�
    int i;
    for ( i = 0; i < dir->file_count; i++) {
        if (strcmp(dir->files[i]->name, name) == 0) {
            return dir->files[i];
        }
    }
    return NULL;
}

void remove_file_from_directory(Directory *dir, const char *name) {//��Ŀ¼��ɾ���ļ�
    int i;
    for ( i = 0; i < dir->file_count; i++) {
        if (strcmp(dir->files[i]->name, name) == 0) {
            free(dir->files[i]->content);//�ͷ��ļ�����
            free(dir->files[i]);//�ͷ��ļ��ռ�
            int j;
            for ( j = i; j < dir->file_count - 1; j++) {
                dir->files[j] = dir->files[j + 1];
            }
            dir->file_count--;
            break;
        }
    }
}

void remove_subdirectory(Directory *dir, const char *name) {//ɾ����Ŀ¼
    int i;
    for ( i = 0; i < dir->subdir_count; i++) {
        if (strcmp(dir->subdirectories[i]->name, name) == 0) {
            free(dir->subdirectories[i]);
            int j;
            for ( j = i; j < dir->subdir_count - 1; j++) {
                dir->subdirectories[j] = dir->subdirectories[j + 1];
            }
            dir->subdir_count--;
            break;
        }
    }
}

void go_back_to_parent_directory() {//���ظ�Ŀ¼
    if (current->parent_directory != NULL) {
        current = current->parent_directory;//��ǰ�ļ��������Ŀ¼
    }
}

int main() {
    root = create_directory("/");//����Ŀ¼
    current = root;
printf("Welcome to the custom shell! \n");
    char command[32], arg1[32], arg2[32];
    while (1) {
        printf("%s> ", current->name);//��ӡ��ǰĿ¼����
        scanf("%s", command);//����Ҫ��

        if (strcmp(command, "ls") == 0) {
            list_directory(current);//�鿴��ǰĿ¼�µ��ļ����ļ�����Ϣ���
        } else if (strcmp(command, "cd") == 0) {//�����¼�Ŀ¼����
            scanf("%s", arg1);//�����¼�Ŀ¼����
            if (strcmp(arg1, "..") == 0) {
                go_back_to_parent_directory();//cd ..�����Ŀ¼
            } else {
                Directory *next = find_subdirectory(current, arg1);//������һ��Ŀ¼
                if (next != NULL) {//����
                    current = next;
                } else {//������-���
                    printf("Directory not found.\n");
                }
            }
        } else if (strcmp(command, "mv") == 0) {//�ƶ��ļ�����
            scanf("%s %s", arg1, arg2);//old�ļ����ݣ�new�ļ�
            File *file = find_file(current, arg1);//����old�ļ�
            if (file != NULL) {
                strcpy(file->name, arg2);
            } else {
                printf("File not found.\n");
            }
        } else if (strcmp(command, "touch") == 0) {//�½��ļ�����
            scanf("%s", arg1);//�����ļ���
            File *file = create_file(arg1, "");//�½��ļ�
            add_file_to_directory(current, file);
        } else if (strcmp(command, "mkdir") == 0) {//�½��ļ�������
            scanf("%s", arg1);//�����ļ�����
            Directory *dir = create_directory(arg1);//�½�Ŀ¼
            add_subdirectory(current, dir);//�����Ŀ¼
        } else if (strcmp(command, "rm") == 0) {//ɾ���ļ�����
            scanf("%s", arg1);//�����ļ���
            remove_file_from_directory(current, arg1);
        } else if (strcmp(command, "rmdir") == 0) {//ɾ���ļ�������
            scanf("%s", arg1);//�����ļ�����
            remove_subdirectory(current, arg1);//��Ŀ¼��ɾ���ļ���
        } else if (strcmp(command, "read") == 0) {//��ĳ�ļ��ڶ�ȡ��Ϣ����
            scanf("%s", arg1);
            File *file = find_file(current, arg1);
            if (file != NULL) {
                printf("%s\n", file->content);
            } else {
                printf("File not found.\n");
            }
        } else if (strcmp(command, "write") == 0) {//��ĳ�ļ���д����Ϣ����
            scanf("%s", arg1);
            File *file = find_file(current, arg1);
            if (file != NULL) {
                char buffer[1024];
                scanf(" %[^\n]", buffer);//д�����Ϣ
                file->content = strdup(buffer);
                file->size = strlen(buffer);
            } else {
                printf("File not found.\n");
            }
        } else if (strcmp(command, "exit") == 0) {//�˳��ļ�ϵͳ����
            break;
        } else {
            printf("Unknown command: %s\n", command);
        }
    }

    return 0;
}
/*/> mkdir test
/> cd test
/test> touch example.txt
/test> write example.txt This is an example.
/test> ls
  File: example.txt (21 bytes)
/test> read example.txt
This is an example.
/test> mv example.txt new_example.txt
/test> ls
  File: new_example.txt (21 bytes)
/test> cd ..
/> ls
  Subdirectory: test
/> exit

*/
