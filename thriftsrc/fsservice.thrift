namespace java projeto3sd 
namespace py projeto3sd



struct TPage
{
    1:i64 creation,
    2:i64 modification,
    3:i32 version,
    4:binary data,
}


service FSService
{
    TPage GetFile(1:string path),
    list<TPage> ListFiles(1:string path),
    bool AddFile(1:string path, 2:binary data),
    TPage UpdateFile(1:string path, 2:binary data),
    TPage DeleteFile(1:string path),
    TPage UpdateVersion(1:string path, 2:binary data, 3:i32 version),
    TPage DeleteVersion(1:string path, 2:i32 version),
}
